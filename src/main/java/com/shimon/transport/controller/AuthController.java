package com.shimon.transport.controller;

import com.shimon.transport.dto.ChangePasswordFormDto;
import com.shimon.transport.entity.User;
import com.shimon.transport.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

/**
 * Authentication controller.
 * Login: phone number + password (BCrypt).
 * Change password: available to all logged-in users.
 */
@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository,
                          BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ----------------------------------------------------------------
    // Login — show form
    // ----------------------------------------------------------------

    @GetMapping("/login")
    public String showLogin() {
        return "auth/login";
    }

    // ----------------------------------------------------------------
    // Login — handle POST
    // ----------------------------------------------------------------

    @PostMapping("/login")
    public String login(@RequestParam("phoneNumber") String phoneNumber,
                        @RequestParam("password") String password,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {

        Optional<User> userOpt = userRepository.findByPhoneNumber(phoneNumber.trim());

        if (userOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Phone number not found. Please contact the administrator.");
            return "redirect:/auth/login";
        }

        User user = userOpt.get();

        if (!user.isActive()) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Your account is inactive. Please contact the manager.");
            return "redirect:/auth/login";
        }

        // If no password is set yet, direct the user to set one
        if (user.getPasswordHash() == null || user.getPasswordHash().isBlank()) {
            // Store phone temporarily so the set-password page knows who is setting up
            session.setAttribute("pendingUserId", user.getId());
            redirectAttributes.addFlashAttribute("infoMessage",
                    "Welcome! Please set a password for your account before continuing.");
            return "redirect:/auth/set-password";
        }

        // Verify password
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Incorrect password. Please try again.");
            return "redirect:/auth/login";
        }

        // Establish session
        session.setAttribute("userId", user.getId());
        session.setAttribute("userName", user.getFullName());
        session.setAttribute("isManager", user.isShiftManager() || user.isSystemAdmin());
        session.setAttribute("isSystemAdmin", user.isSystemAdmin());
        session.setAttribute("isDriver", user.isCanDrive());
        session.setAttribute("canRequestTransport", user.isCanRequestTransport());

        // Route to appropriate dashboard
        if (user.isShiftManager() || user.isSystemAdmin()) {
            return "redirect:/manager/dashboard";
        } else if (user.isCanDrive() && !user.isCanRequestTransport()) {
            return "redirect:/driver/dashboard";
        } else {
            return "redirect:/employee/dashboard";
        }
    }

    // ----------------------------------------------------------------
    // Set password — first-time setup (no session required, uses pendingUserId)
    // ----------------------------------------------------------------

    @GetMapping("/set-password")
    public String showSetPassword(HttpSession session, Model model,
                                  RedirectAttributes redirectAttributes) {
        Long pendingId = (Long) session.getAttribute("pendingUserId");
        if (pendingId == null) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Session expired. Please log in again.");
            return "redirect:/auth/login";
        }
        model.addAttribute("form", new ChangePasswordFormDto());
        model.addAttribute("rulesHint", ChangePasswordFormDto.PASSWORD_RULES_HINT);
        model.addAttribute("pageTitle", "Set Your Password");
        model.addAttribute("isFirstTime", true);
        return "auth/change-password";
    }

    @PostMapping("/set-password")
    public String saveSetPassword(@Valid @ModelAttribute("form") ChangePasswordFormDto form,
                                  BindingResult bindingResult,
                                  HttpSession session,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        Long pendingId = (Long) session.getAttribute("pendingUserId");
        if (pendingId == null) {
            return "redirect:/auth/login";
        }

        model.addAttribute("rulesHint", ChangePasswordFormDto.PASSWORD_RULES_HINT);
        model.addAttribute("pageTitle", "Set Your Password");
        model.addAttribute("isFirstTime", true);

        if (bindingResult.hasErrors()) {
            return "auth/change-password";
        }

        if (!form.getNewPassword().equals(form.getConfirmPassword())) {
            model.addAttribute("confirmError", "Passwords do not match.");
            return "auth/change-password";
        }

        User user = userRepository.findById(pendingId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPasswordHash(passwordEncoder.encode(form.getNewPassword()));
        userRepository.save(user);

        session.removeAttribute("pendingUserId");
        redirectAttributes.addFlashAttribute("successMessage",
                "Password set successfully. Please log in.");
        return "redirect:/auth/login";
    }

    // ----------------------------------------------------------------
    // Change password — for already-logged-in users
    // ----------------------------------------------------------------

    @GetMapping("/change-password")
    public String showChangePassword(HttpSession session, Model model) {
        if (session.getAttribute("userId") == null) return "redirect:/auth/login";
        model.addAttribute("form", new ChangePasswordFormDto());
        model.addAttribute("rulesHint", ChangePasswordFormDto.PASSWORD_RULES_HINT);
        model.addAttribute("pageTitle", "Change Password");
        model.addAttribute("isFirstTime", false);
        return "auth/change-password";
    }

    @PostMapping("/change-password")
    public String saveChangePassword(@Valid @ModelAttribute("form") ChangePasswordFormDto form,
                                     BindingResult bindingResult,
                                     HttpSession session,
                                     Model model,
                                     RedirectAttributes redirectAttributes) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/auth/login";

        model.addAttribute("rulesHint", ChangePasswordFormDto.PASSWORD_RULES_HINT);
        model.addAttribute("pageTitle", "Change Password");
        model.addAttribute("isFirstTime", false);

        if (bindingResult.hasErrors()) {
            return "auth/change-password";
        }

        if (!form.getNewPassword().equals(form.getConfirmPassword())) {
            model.addAttribute("confirmError", "Passwords do not match.");
            return "auth/change-password";
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPasswordHash(passwordEncoder.encode(form.getNewPassword()));
        userRepository.save(user);

        redirectAttributes.addFlashAttribute("successMessage", "Password updated successfully.");
        return "redirect:/employee/profile";
    }

    // ----------------------------------------------------------------
    // Logout
    // ----------------------------------------------------------------

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth/login";
    }
}
