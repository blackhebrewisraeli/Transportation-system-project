package com.shimon.transport.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * Form-backing DTO for creating a new transport event (manager only).
 */
public class CreateTransportEventFormDto {

    @NotNull(message = "Date is required")
    @FutureOrPresent(message = "Event date must be today or in the future")
    private LocalDate eventDate;

    @NotNull(message = "Shift type is required")
    private Long shiftTypeId;

    private String notes;

    // -- Constructors --

    public CreateTransportEventFormDto() {
    }

    // -- Getters and Setters --

    public LocalDate getEventDate() { return eventDate; }
    public void setEventDate(LocalDate eventDate) { this.eventDate = eventDate; }

    public Long getShiftTypeId() { return shiftTypeId; }
    public void setShiftTypeId(Long shiftTypeId) { this.shiftTypeId = shiftTypeId; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
