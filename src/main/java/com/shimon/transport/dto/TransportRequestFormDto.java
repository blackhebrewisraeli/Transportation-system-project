package com.shimon.transport.dto;

import com.shimon.transport.enums.RequestDirection;
import jakarta.validation.constraints.NotNull;

/**
 * Form-backing DTO for submitting a new transport request.
 * Bound to the Create Request form in Thymeleaf.
 */
public class TransportRequestFormDto {

    @NotNull(message = "Transport event must be selected")
    private Long transportEventId;

    @NotNull(message = "Please select a direction (Pickup / Drop-off / Both)")
    private RequestDirection direction;

    // -- Constructors --

    public TransportRequestFormDto() {
    }

    // -- Getters and Setters --

    public Long getTransportEventId() {
        return transportEventId;
    }

    public void setTransportEventId(Long transportEventId) {
        this.transportEventId = transportEventId;
    }

    public RequestDirection getDirection() {
        return direction;
    }

    public void setDirection(RequestDirection direction) {
        this.direction = direction;
    }
}
