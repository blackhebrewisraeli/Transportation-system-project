package com.shimon.transport.dto;

import jakarta.validation.constraints.NotNull;

/**
 * Form-backing DTO for assigning a driver to a transport event (manager only).
 */
public class AssignDriverFormDto {

    @NotNull(message = "Please select a driver")
    private Long driverId;

    // -- Constructors --

    public AssignDriverFormDto() {
    }

    // -- Getters and Setters --

    public Long getDriverId() { return driverId; }
    public void setDriverId(Long driverId) { this.driverId = driverId; }
}
