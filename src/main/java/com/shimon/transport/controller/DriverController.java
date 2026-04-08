package com.shimon.transport.controller;

import com.shimon.transport.service.DriverService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Handles driver-facing views: list of assigned events and passenger list per event.
 */
@Controller
@RequestMapping("/driver")
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    // ----------------------------------------------------------------
    // Driver dashboard — list of assigned events
    // ----------------------------------------------------------------

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Long userId = getSessionUserId(session);
        if (userId == null || !isDriver(session)) return "redirect:/auth/login";

        model.addAttribute("assignedEvents", driverService.getAssignedEvents(userId));
        return "driver/dashboard";
    }

    // ----------------------------------------------------------------
    // Passenger list for a specific assigned event
    // ----------------------------------------------------------------

    @GetMapping("/event/{eventId}")
    public String eventDetail(@PathVariable Long eventId,
                              HttpSession session,
                              Model model) {
        Long userId = getSessionUserId(session);
        if (userId == null || !isDriver(session)) return "redirect:/auth/login";

        model.addAttribute("event", driverService.getAssignedEventDetail(eventId, userId));
        return "driver/passenger-list";
    }

    // ----------------------------------------------------------------
    // Helpers
    // ----------------------------------------------------------------

    private Long getSessionUserId(HttpSession session) {
        return (Long) session.getAttribute("userId");
    }

    private boolean isDriver(HttpSession session) {
        Boolean isDriver = (Boolean) session.getAttribute("isDriver");
        return Boolean.TRUE.equals(isDriver);
    }
}
