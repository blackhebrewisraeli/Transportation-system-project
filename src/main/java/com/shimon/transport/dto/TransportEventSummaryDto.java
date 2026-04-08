package com.shimon.transport.dto;

import com.shimon.transport.enums.TransportEventStatus;

import java.time.LocalDate;

/**
 * Lightweight read-only view of a transport event for listing on the employee dashboard.
 */
public class TransportEventSummaryDto {

    private Long id;
    private LocalDate eventDate;
    private String shiftName;
    private String shiftHours;
    private TransportEventStatus status;
    private boolean hasDriver;
    private int requestCount;
    private boolean currentUserRequested;

    // -- Constructors --

    public TransportEventSummaryDto() {
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

    public String getShiftHours() {
        return shiftHours;
    }

    public void setShiftHours(String shiftHours) {
        this.shiftHours = shiftHours;
    }

    public TransportEventStatus getStatus() {
        return status;
    }

    public void setStatus(TransportEventStatus status) {
        this.status = status;
    }

    public boolean isHasDriver() {
        return hasDriver;
    }

    public void setHasDriver(boolean hasDriver) {
        this.hasDriver = hasDriver;
    }

    public int getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(int requestCount) {
        this.requestCount = requestCount;
    }

    public boolean isCurrentUserRequested() {
        return currentUserRequested;
    }

    public void setCurrentUserRequested(boolean currentUserRequested) {
        this.currentUserRequested = currentUserRequested;
    }
}
