package com.shimon.transport.service;

import com.shimon.transport.dto.PassengerRowDto;
import com.shimon.transport.dto.TransportEventDetailDto;
import com.shimon.transport.entity.TransportEvent;
import com.shimon.transport.entity.TransportRequest;
import com.shimon.transport.enums.RequestStatus;
import com.shimon.transport.exception.BusinessRuleException;
import com.shimon.transport.exception.ResourceNotFoundException;
import com.shimon.transport.repository.TransportEventRepository;
import com.shimon.transport.repository.TransportRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class DriverService {

    private final TransportEventRepository eventRepository;
    private final TransportRequestRepository requestRepository;

    public DriverService(TransportEventRepository eventRepository,
                         TransportRequestRepository requestRepository) {
        this.eventRepository = eventRepository;
        this.requestRepository = requestRepository;
    }

    /**
     * Returns all transport events assigned to this driver, ordered by date.
     */
    public List<TransportEventDetailDto> getAssignedEvents(Long driverId) {
        return eventRepository.findByAssignedDriverIdOrderByEventDateAsc(driverId)
                .stream()
                .map(event -> toDetail(event, getActiveRequests(event.getId())))
                .collect(Collectors.toList());
    }

    /**
     * Returns the full detail of a single event assigned to this driver.
     * Throws if the event is not assigned to the requesting driver.
     */
    public TransportEventDetailDto getAssignedEventDetail(Long eventId, Long driverId) {
        TransportEvent event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("TransportEvent", eventId));

        if (event.getAssignedDriver() == null || !event.getAssignedDriver().getId().equals(driverId)) {
            throw new BusinessRuleException("This event is not assigned to you.");
        }

        return toDetail(event, getActiveRequests(eventId));
    }

    // ----------------------------------------------------------------
    // Helpers
    // ----------------------------------------------------------------

    private List<TransportRequest> getActiveRequests(Long eventId) {
        return requestRepository.findByTransportEventIdAndStatusNot(eventId, RequestStatus.CANCELLED);
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
