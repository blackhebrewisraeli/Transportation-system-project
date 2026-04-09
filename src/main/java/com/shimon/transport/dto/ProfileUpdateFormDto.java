package com.shimon.transport.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Form DTO for employee profile update.
 * - addressText is mandatory for all users (required for transport operations).
 * - fullName is only applied when the submitting user is a system admin.
 * - email is optional for all users.
 */
public class ProfileUpdateFormDto {

    @Size(max = 100, message = "Full name must not exceed 100 characters")
    private String fullName;

    @NotBlank(message = "Home address is required")
    @Size(max = 300, message = "Address must not exceed 300 characters")
    private String addressText;

    @Email(message = "Please enter a valid email address")
    @Size(max = 150, message = "Email must not exceed 150 characters")
    private String email;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

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
