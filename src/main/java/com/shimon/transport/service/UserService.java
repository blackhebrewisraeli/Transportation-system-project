package com.shimon.transport.service;

import com.shimon.transport.dto.ProfileUpdateFormDto;
import com.shimon.transport.entity.User;
import com.shimon.transport.exception.ResourceNotFoundException;
import com.shimon.transport.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * Updates the mutable profile fields of a user (address and email).
     * Full name and phone number are not editable by the user — admin only.
     */
    public void updateProfile(Long userId, ProfileUpdateFormDto form) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        String address = form.getAddressText();
        user.setAddressText(address != null && !address.isBlank() ? address.trim() : null);

        String email = form.getEmail();
        user.setEmail(email != null && !email.isBlank() ? email.trim() : null);

        userRepository.save(user);
    }
}
