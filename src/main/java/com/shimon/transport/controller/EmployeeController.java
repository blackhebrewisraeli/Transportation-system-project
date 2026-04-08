package com.shimon.transport.controller;

import com.shimon.transport.dto.ProfileUpdateFormDto;
import com.shimon.transport.dto.TransportRequestFormDto;
import com.shimon.transport.entity.User;
import com.shimon.transport.service.TransportRequestService;
import com.shimon.transport.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Handles employee-facing views: dashboard, submit request, cancel request, profile.
 * User identity comes from the HTTP session (mock auth for MVP).
 * Session key: "userId" (Long)
 */
@Controller
@RequestMapping("/employee")
public class EmployeeController {

    private final TransportRequestService requestService;
    private final UserService userService;

    public EmployeeController(TransportRequestService requestService, UserService userService) {
        this.requestService = requestService;
        this.userService = userService;
    }

    // ----------------------------------------------------------------
    // Dashboard — list open events and own requests
    // ----------------------------------------------------------------

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Long userId = getSessionUserId(session);
        if (userId == null) return "redirect:/auth/login";

        model.addAttribute("openEvents", requestService.getOpenEventsForUser(userId));
        model.addAttribute("myRequests", requestService.getRequestsForUser(userId));
        return "employee/dashboard";
    }

    // ----------------------------------------------------------------
    // Submit request — show form
    // ----------------------------------------------------------------

    @GetMapping("/request/new")
    public String showRequestForm(@RequestParam("eventId") Long eventId,
                                  HttpSession session,
                                  Model model) {
        Long userId = getSessionUserId(session);
        if (userId == null) return "redirect:/auth/login";

        TransportRequestFormDto form = new TransportRequestFormDto();
        form.setTransportEventId(eventId);

        model.addAttribute("form", form);
        model.addAttribute("directions", com.shimon.transport.enums.RequestDirection.values());
        return "employee/request-form";
    }

    // ----------------------------------------------------------------
    // Submit request — handle POST
    // ----------------------------------------------------------------

    @PostMapping("/request/new")
    public String submitRequest(@Valid @ModelAttribute("form") TransportRequestFormDto form,
                                BindingResult bindingResult,
                                HttpSession session,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        Long userId = getSessionUserId(session);
        if (userId == null) return "redirect:/auth/login";

        if (bindingResult.hasErrors()) {
            model.addAttribute("directions", com.shimon.transport.enums.RequestDirection.values());
            return "employee/request-form";
        }

        requestService.submitRequest(userId, form);
        redirectAttributes.addFlashAttribute("successMessage", "Your transport request was submitted successfully.");
        return "redirect:/employee/dashboard";
    }

    // ----------------------------------------------------------------
    // Cancel request
    // ----------------------------------------------------------------

    @PostMapping("/request/{requestId}/cancel")
    public String cancelRequest(@PathVariable Long requestId,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        Long userId = getSessionUserId(session);
        if (userId == null) return "redirect:/auth/login";

        requestService.cancelRequest(requestId, userId);
        redirectAttributes.addFlashAttribute("successMessage", "Your request has been cancelled.");
        return "redirect:/employee/dashboard";
    }

    // ----------------------------------------------------------------
    // Profile — show
    // ----------------------------------------------------------------

    @GetMapping("/profile")
    public String showProfile(HttpSession session, Model model) {
        Long userId = getSessionUserId(session);
        if (userId == null) return "redirect:/auth/login";

        User user = userService.getById(userId);

        ProfileUpdateFormDto form = new ProfileUpdateFormDto();
        form.setAddressText(user.getAddressText());
        form.setEmail(user.getEmail());

        model.addAttribute("user", user);
        model.addAttribute("form", form);
        return "employee/profile";
    }

    // ----------------------------------------------------------------
    // Profile — update
    // ----------------------------------------------------------------

    @PostMapping("/profile")
    public String updateProfile(@Valid @ModelAttribute("form") ProfileUpdateFormDto form,
                                BindingResult bindingResult,
                                HttpSession session,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        Long userId = getSessionUserId(session);
        if (userId == null) return "redirect:/auth/login";

        if (bindingResult.hasErrors()) {
            User user = userService.getById(userId);
            model.addAttribute("user", user);
            return "employee/profile";
        }

        userService.updateProfile(userId, form);
        redirectAttributes.addFlashAttribute("successMessage", "Your profile has been updated.");
        return "redirect:/employee/profile";
    }

    // ----------------------------------------------------------------
    // Helper
    // ----------------------------------------------------------------

    private Long getSessionUserId(HttpSession session) {
        return (Long) session.getAttribute("userId");
    }
}
