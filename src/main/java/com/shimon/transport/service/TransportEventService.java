package com.shimon.transport.service;

import com.shimon.transport.dto.*;
import com.shimon.transport.entity.ShiftType;
import com.shimon.transport.entity.TransportEvent;
import com.shimon.transport.entity.TransportRequest;
import com.shimon.transport.entity.User;
import com.shimon.transport.enums.RequestStatus;
import com.shimon.transport.enums.TransportEventStatus;
import com.shimon.transport.exception.BusinessRuleException;
import com.shimon.transport.exception.ResourceNotFoundException;
import com.shimon.transport.repository.ShiftTypeRepository;
import com.shimon.transport.repository.TransportEventRepository;
import com.shimon.transport.repository.TransportRequestRepository;
import com.shimon.transport.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TransportEventService {

    private final TransportEventRepository eventRepository;
    private final TransportRequestRepository requestRepository;
    private final ShiftTypeRepository shiftTypeRepository;
    private final UserRepository userRepository;

    public TransportEventService(TransportEventRepository eventRepository,
                                 TransportRequestRepository requestRepository,
                                 ShiftTypeRepository shiftTypeRepository,
                                 UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.requestRepository = requestRepository;
        this.shiftTypeRepository = shiftTypeRepository;
        this.userRepository = userRepository;
    }

    // ----------------------------------------------------------------
    // Manager: list all events (upcoming)
    // ----------------------------------------------------------------

    /**
     * Returns all upcoming transport events (today and forward), ordered by date.
     */
    @Transactional(readOnly = true)
    public List<TransportEventSummaryDto> getAllUpcomingEvents() {
        LocalDate today = LocalDate.now();
        return eventRepository
                .findByEventDateBetweenOrderByEventDateAscShiftTypeIdAsc(today, today.plusMonths(3))
                .stream()
                .map(this::toSummary)
                .collect(Collectors.toList());
    }

    // ----------------------------------------------------------------
    // Manager: view event detail
    // ----------------------------------------------------------------

    @Transactional(readOnly = true)
    public TransportEventDetailDto getEventDetail(Long eventId) {
        TransportEvent event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("TransportEvent", eventId));

        List<TransportRequest> requests = requestRepository
                .findByTransportEventIdAndStatusNot(eventId, RequestStatus.CANCELLED);

        return toDetail(event, requests);
    }

    // ----------------------------------------------------------------
    // Manager: create a new transport event
    // ----------------------------------------------------------------

    public TransportEvent createEvent(CreateTransportEventFormDto form) {
        ShiftType shiftType = shiftTypeRepository.findById(form.getShiftTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("ShiftType", form.getShiftTypeId()));

        if (eventRepository.existsByEventDateAndShiftTypeId(form.getEventDate(), shiftType.getId())) {
            throw new BusinessRuleException(
                    "A transport event already exists for " + form.getEventDate() + " / " + shiftType.getName());
        }

        TransportEvent event = new TransportEvent();
        event.setEventDate(form.getEventDate());
        event.setShiftType(shiftType);
        event.setNotes(form.getNotes());
        event.setStatus(TransportEventStatus.OPEN);
        event.setTransportRequired(true);

        return eventRepository.save(event);
    }

    // ----------------------------------------------------------------
    // Manager: assign driver
    // ----------------------------------------------------------------

    public void assignDriver(Long eventId, AssignDriverFormDto form) {
        TransportEvent event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("TransportEvent", eventId));

        User driver = userRepository.findById(form.getDriverId())
                .orElseThrow(() -> new ResourceNotFoundException("User", form.getDriverId()));

        if (!driver.isCanDrive()) {
            throw new BusinessRuleException(driver.getFullName() + " is not permitted to drive.");
        }
        if (!driver.isActive()) {
            throw new BusinessRuleException("Cannot assign an inactive user as driver.");
        }

        event.setAssignedDriver(driver);
        eventRepository.save(event);
    }

    // ----------------------------------------------------------------
    // Manager: change event status
    // ----------------------------------------------------------------

    public void updateEventStatus(Long eventId, TransportEventStatus newStatus) {
        TransportEvent event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("TransportEvent", eventId));
        event.setStatus(newStatus);
        eventRepository.save(event);
    }

    // ----------------------------------------------------------------
    // Shared: list active shift types (for forms)
    // ----------------------------------------------------------------

    @Transactional(readOnly = true)
    public List<ShiftType> getActiveShiftTypes() {
        return shiftTypeRepository.findByActiveTrue();
    }

    // ----------------------------------------------------------------
    // Shared: list eligible drivers (for assign-driver form)
    // ----------------------------------------------------------------

    @Transactional(readOnly = true)
    public List<User> getEligibleDrivers() {
        return userRepository.findByCanDriveTrueAndActiveTrue();
    }

    // ----------------------------------------------------------------
    // Mapping helpers
    // ----------------------------------------------------------------

    private TransportEventSummaryDto toSummary(TransportEvent event) {
        TransportEventSummaryDto dto = new TransportEventSummaryDto();
        dto.setId(event.getId());
        dto.setEventDate(event.getEventDate());
        dto.setShiftName(event.getShiftType().getName().name());
        dto.setShiftHours(event.getShiftType().getStartTime() + " - " + event.getShiftType().getEndTime());
        dto.setStatus(event.getStatus());
        dto.setHasDriver(event.getAssignedDriver() != null);

        List<TransportRequest> active = requestRepository
                .findByTransportEventIdAndStatusNot(event.getId(), RequestStatus.CANCELLED);
        dto.setRequestCount(active.size());

        return dto;
    }

    private TransportEventDetailDto toDetail(TransportEvent event, List<TransportRequest> requests) {
        TransportEventDetailDto dto = new TransportEventDetailDto();
        dto.setId(event.getId());
        dto.setEventDate(event.getEventDate());
        dto.setShiftName(event.getShiftType().getName().name());
        dto.setShiftHours(event.getShiftType().getStartTime() + " - " + event.getShiftType().getEndTime());
        dto.setStatus(event.getStatus());
        dto.setNotes(event.getNotes());

        if (event.getAssignedDriver() != null) {
            dto.setAssignedDriverName(event.getAssignedDriver().getFullName());
            dto.setAssignedDriverId(event.getAssignedDriver().getId());
        }

        List<PassengerRowDto> passengers = requests.stream()
                .map(r -> {
                    PassengerRowDto row = new PassengerRowDto();
                    row.setRequestId(r.getId());
                    row.setFullName(r.getUser().getFullName());
                    row.setPhoneNumber(r.getUser().getPhoneNumber());
                    row.setAddressText(r.getUser().getAddressText());
                    row.setDirection(r.getDirection());
                    row.setStatus(r.getStatus());
                    return row;
                })
                .collect(Collectors.toList());

        dto.setPassengers(passengers);
        return dto;
    }
}
