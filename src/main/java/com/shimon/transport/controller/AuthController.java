package com.shimon.transport.controller;

import com.shimon.transport.entity.User;
import com.shimon.transport.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

/**
 * Mock authentication controller for MVP.
 * Identifies users by phone number only — no OTP, no password.
 * Replace with real OTP flow in Phase 2.
 */
@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    public String showLogin() {
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("phoneNumber") String phoneNumber,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {

        Optional<User> userOpt = userRepository.findByPhoneNumber(phoneNumber.trim());

        if (userOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Phone number not found. Please contact the administrator.");
            return "redirect:/auth/login";
        }

        User user = userOpt.get();

        if (!user.isActive()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Your account is inactive. Please contact the manager.");
            return "redirect:/auth/login";
        }

        // Store user identity in session
        session.setAttribute("userId", user.getId());
        session.setAttribute("userName", user.getFullName());
        session.setAttribute("isManager", user.isShiftManager() || user.isSystemAdmin());
        session.setAttribute("isSystemAdmin", user.isSystemAdmin());
        session.setAttribute("isDriver", user.isCanDrive());

        // Route to appropriate dashboard
        if (user.isShiftManager() || user.isSystemAdmin()) {
            return "redirect:/manager/dashboard";
        } else if (user.isCanDrive() && !user.isCanRequestTransport()) {
            return "redirect:/driver/dashboard";
        } else {
            return "redirect:/employee/dashboard";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth/login";
    }
}
