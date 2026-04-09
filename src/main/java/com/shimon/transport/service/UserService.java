package com.shimon.transport.service;

import com.shimon.transport.dto.ProfileUpdateFormDto;
import com.shimon.transport.entity.User;
import com.shimon.transport.exception.ResourceNotFoundException;
import com.shimon.transport.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Handles user profile operations available to the user themselves.
 * Admin-level user management (create, deactivate, assign roles) will live in a future AdminService.
 */
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Returns a user by ID, or throws ResourceNotFoundException if not found.
     */
    @Transactional(readOnly = true)
    public User getById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
    }

    /**
     * Returns all users ordered by full name — used by the admin user-management page.
     */
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAllByOrderByFullNameAsc();
    }

    /**
     * Updates only the full name of a user. System-admin privilege only.
     */
    public void updateUserName(Long userId, String newName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        user.setFullName(newName);
        userRepository.save(user);
    }

    /**
     * Updates the mutable profile fields of a user.
     * - addressText: required for all users.
     * - email: optional for all users.
     * - fullName: only updated when isAdmin is true (system admin privilege).
     */
    public void updateProfile(Long userId, ProfileUpdateFormDto form, boolean isAdmin) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        // Address — required, already validated by @NotBlank in the DTO
        user.setAddressText(form.getAddressText().trim());

        // Email — optional
        String email = form.getEmail();
        user.setEmail(email != null && !email.isBlank() ? email.trim() : null);

        // Full name — system admin only
        if (isAdmin) {
            String fullName = form.getFullName();
            if (fullName != null && !fullName.isBlank()) {
                user.setFullName(fullName.trim());
            }
        }

        userRepository.save(user);
    }
}
