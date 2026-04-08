package com.shimon.transport.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

/**
 * Form DTO for employee profile update.
 * Employees may update their address and email.
 * Full name and phone number are managed by admin only.
 */
public class ProfileUpdateFormDto {

    @Size(max = 200, message = "Address must not exceed 200 characters")
    private String addressText;

    @Email(message = "Please enter a valid email address")
    @Size(max = 150, message = "Email must not exceed 150 characters")
    private String email;

    public String getAddressText() {
        return addressText;
    }

    public void setAddressText(String addressText) {
        this.addressText = addressText;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
