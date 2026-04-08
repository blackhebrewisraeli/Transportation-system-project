package com.shimon.transport.dto;

import com.shimon.transport.enums.RequestDirection;
import com.shimon.transport.enums.RequestStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Read-only view of a single transport request, used in employee and manager views.
 */
public class TransportRequestSummaryDto {

    private Long id;
    private LocalDate eventDate;
    private String shiftName;
    private RequestDirection direction;
    private RequestStatus status;
    private LocalDateTime createdAt;

    // -- Constructors --

    public TransportRequestSummaryDto() {
    }

    // -- Getters and Setters --

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public String getShiftName() {
        return shiftName;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    public RequestDirection getDirection() {
        return direction;
    }

    public void setDirection(RequestDirection direction) {
        this.direction = direction;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
