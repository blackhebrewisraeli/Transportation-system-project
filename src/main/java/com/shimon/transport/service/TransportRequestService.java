package com.shimon.transport.service;

import com.shimon.transport.dto.TransportEventSummaryDto;
import com.shimon.transport.dto.TransportRequestFormDto;
import com.shimon.transport.dto.TransportRequestSummaryDto;
import com.shimon.transport.entity.TransportEvent;
import com.shimon.transport.entity.TransportRequest;
import com.shimon.transport.entity.User;
import com.shimon.transport.enums.RequestStatus;
import com.shimon.transport.enums.TransportEventStatus;
import com.shimon.transport.exception.BusinessRuleException;
import com.shimon.transport.exception.ResourceNotFoundException;
import com.shimon.transport.repository.TransportEventRepository;
import com.shimon.transport.repository.TransportRequestRepository;
import com.shimon.transport.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TransportRequestService {

    private final TransportRequestRepository requestRepository;
    private final TransportEventRepository eventRepository;
    private final UserRepository userRepository;

    public TransportRequestService(TransportRequestRepository requestRepository,
                                   TransportEventRepository eventRepository,
                                   UserRepository userRepository) {
        this.requestRepository = requestRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    // ----------------------------------------------------------------
    // Employee: view open events
    // ----------------------------------------------------------------

    /**
     * Returns all OPEN transport events, enriched with whether the given user already has a request.
     */
    @Transactional(readOnly = true)
    public List<TransportEventSummaryDto> getOpenEventsForUser(Long userId) {
        List<TransportEvent> events = eventRepository.findByStatusOrderByEventDateAsc(TransportEventStatus.OPEN);

        return events.stream()
                .map(event -> toEventSummary(event, userId))
                .collect(Collectors.toList());
    }

    // ----------------------------------------------------------------
    // Employee: submit a request
    // ----------------------------------------------------------------

    /**
     * Submits a new transport request for the given user.
     * Enforces: user must be active, can_request_transport, event must be OPEN, no duplicate.
     */
    public void submitRequest(Long userId, TransportRequestFormDto form) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        if (!user.isActive()) {
            throw new BusinessRuleException("Your account is inactive. Please contact the manager.");
        }
        if (!user.isCanRequestTransport()) {
            throw new BusinessRuleException("You are not permitted to request transport.");
        }

        TransportEvent event = eventRepository.findById(form.getTransportEventId())
                .orElseThrow(() -> new ResourceNotFoundException("TransportEvent", form.getTransportEventId()));

        if (event.getStatus() != TransportEventStatus.OPEN) {
            throw new BusinessRuleException("This transport event is no longer open for requests.");
        }

        if (requestRepository.existsByUserIdAndTransportEventId(userId, event.getId())) {
            throw new BusinessRuleException("You have already submitted a request for this event.");
        }

        TransportRequest request = new TransportRequest();
        request.setUser(user);
        request.setTransportEvent(event);
        request.setDirection(form.getDirection());
        request.setStatus(RequestStatus.PENDING);

        requestRepository.save(request);
    }

    // ----------------------------------------------------------------
    // Employee: view own requests
    // ----------------------------------------------------------------

    /**
     * Returns all requests submitted by the given user, ordered by event date descending.
     */
    @Transactional(readOnly = true)
    public List<TransportRequestSummaryDto> getRequestsForUser(Long userId) {
        return requestRepository.findByUserId(userId).stream()
                .map(this::toRequestSummary)
                .collect(Collectors.toList());
    }

    // ----------------------------------------------------------------
    // Employee: cancel a request
    // ----------------------------------------------------------------

    /**
     * Cancels a transport request. Only the owning user may cancel their own request.
     * Request must not already be cancelled.
     */
    public void cancelRequest(Long requestId, Long userId) {
        TransportRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("TransportRequest", requestId));

        if (!request.getUser().getId().equals(userId)) {
            throw new BusinessRuleException("You can only cancel your own requests.");
        }

        if (request.getStatus() == RequestStatus.CANCELLED) {
            throw new BusinessRuleException("This request is already cancelled.");
        }

        if (request.getTransportEvent().getStatus() != TransportEventStatus.OPEN) {
            throw new BusinessRuleException("Requests can only be cancelled while the event is still open.");
        }

        request.setStatus(RequestStatus.CANCELLED);
        requestRepository.save(request);
    }

    // ----------------------------------------------------------------
    // Mapping helpers
    // ----------------------------------------------------------------

    private TransportEventSummaryDto toEventSummary(TransportEvent event, Long userId) {
        TransportEventSummaryDto dto = new TransportEventSummaryDto();
        dto.setId(event.getId());
        dto.setEventDate(event.getEventDate());
        dto.setShiftName(event.getShiftType().getName().name());
        dto.setShiftHours(event.getShiftType().getStartTime() + " - " + event.getShiftType().getEndTime());
        dto.setStatus(event.getStatus());
        dto.setHasDriver(event.getAssignedDriver() != null);

        List<TransportRequest> activeRequests = requestRepository
                .findByTransportEventIdAndStatusNot(event.getId(), RequestStatus.CANCELLED);
        dto.setRequestCount(activeRequests.size());

        boolean alreadyRequested = requestRepository.existsByUserIdAndTransportEventId(userId, event.getId());
        dto.setCurrentUserRequested(alreadyRequested);

        return dto;
    }

    private TransportRequestSummaryDto toRequestSummary(TransportRequest request) {
        TransportRequestSummaryDto dto = new TransportRequestSummaryDto();
        dto.setId(request.getId());
        dto.setEventDate(request.getTransportEvent().getEventDate());
        dto.setShiftName(request.getTransportEvent().getShiftType().getName().name());
        dto.setDirection(request.getDirection());
        dto.setStatus(request.getStatus());
        dto.setCreatedAt(request.getCreatedAt());
        return dto;
    }
}
