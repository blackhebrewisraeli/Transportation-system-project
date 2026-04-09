package com.shimon.transport.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Form DTO for setting or changing a user's password.
 *
 * Password rules (enforced by @Pattern):
 *   - Minimum 8 characters
 *   - At least one uppercase letter (A-Z)
 *   - At least one lowercase letter (a-z)
 *   - At least one digit (0-9)
 *   - At least one special character from: @$!%*?&#^()_+\-=[]{}
 *
 * Confirmation matching is validated manually in the service / controller
 * because Jakarta Validation does not support cross-field comparison natively.
 */
public class ChangePasswordFormDto {

    public static final String PASSWORD_PATTERN =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#^()_+\\-=\\[\\]{};':\"|,.<>]).{8,}$";

    public static final String PASSWORD_RULES_HINT =
            "At least 8 characters · uppercase & lowercase · digit · special character (@$!%*?&#...)";

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(regexp = PASSWORD_PATTERN,
             message = "Password must contain uppercase, lowercase, digit, and special character")
    private String newPassword;

    @NotBlank(message = "Please confirm your password")
    private String confirmPassword;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
