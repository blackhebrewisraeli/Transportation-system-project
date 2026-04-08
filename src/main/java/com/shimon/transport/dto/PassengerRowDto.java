package com.shimon.transport.dto;

import com.shimon.transport.enums.RequestDirection;
import com.shimon.transport.enums.RequestStatus;

/**
 * A single row in the passenger list shown to managers and drivers.
 */
public class PassengerRowDto {

    private Long requestId;
    private String fullName;
    private String phoneNumber;
    private String addressText;
    private RequestDirection direction;
    private RequestStatus status;

    // -- Constructors --

    public PassengerRowDto() {
    }

    // -- Getters and Setters --

    public Long getRequestId() { return requestId; }
    public void setRequestId(Long requestId) { this.requestId = requestId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getAddressText() { return addressText; }
    public void setAddressText(String addressText) { this.addressText = addressText; }

    public RequestDirection getDirection() { return direction; }
    public void setDirection(RequestDirection direction) { this.direction = direction; }

    public RequestStatus getStatus() { return status; }
    public void setStatus(RequestStatus status) { this.status = status; }
}
