package com.shimon.transport.dto;

import com.shimon.transport.enums.TransportEventStatus;

import java.time.LocalDate;
import java.util.List;

/**
 * Full detail view of a transport event for the manager dashboard.
 * Includes request list and driver info.
 */
public class TransportEventDetailDto {

    private Long id;
    private LocalDate eventDate;
    private String shiftName;
    private String shiftHours;
    private TransportEventStatus status;
    private String assignedDriverName;
    private Long assignedDriverId;
    private String notes;
    private List<PassengerRowDto> passengers;

    // -- Constructors --

    public TransportEventDetailDto() {
    }

    // -- Getters and Setters --

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getEventDate() { return eventDate; }
    public void setEventDate(LocalDate eventDate) { this.eventDate = eventDate; }

    public String getShiftName() { return shiftName; }
    public void setShiftName(String shiftName) { this.shiftName = shiftName; }

    public String getShiftHours() { return shiftHours; }
    public void setShiftHours(String shiftHours) { this.shiftHours = shiftHours; }

    public TransportEventStatus getStatus() { return status; }
    public void setStatus(TransportEventStatus status) { this.status = status; }

    public String getAssignedDriverName() { return assignedDriverName; }
    public void setAssignedDriverName(String assignedDriverName) { this.assignedDriverName = assignedDriverName; }

    public Long getAssignedDriverId() { return assignedDriverId; }
    public void setAssignedDriverId(Long assignedDriverId) { this.assignedDriverId = assignedDriverId; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public List<PassengerRowDto> getPassengers() { return passengers; }
    public void setPassengers(List<PassengerRowDto> passengers) { this.passengers = passengers; }
}
