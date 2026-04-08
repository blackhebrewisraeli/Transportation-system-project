package com.shimon.transport.controller;

import com.shimon.transport.dto.AssignDriverFormDto;
import com.shimon.transport.dto.CreateTransportEventFormDto;
import com.shimon.transport.enums.TransportEventStatus;
import com.shimon.transport.service.TransportEventService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Handles manager-facing views: event dashboard, event detail,
 * create event, assign driver, change event status.
 */
@Controller
@RequestMapping("/manager")
public class ManagerController {

    private final TransportEventService eventService;

    public ManagerController(TransportEventService eventService) {
        this.eventService = eventService;
    }

    // ----------------------------------------------------------------
    // Manager dashboard — list of upcoming events
    // ----------------------------------------------------------------

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (!isManager(session)) return "redirect:/auth/login";

        model.addAttribute("events", eventService.getAllUpcomingEvents());
        return "manager/dashboard";
    }

    // ----------------------------------------------------------------
    // Event detail — requests list + assign driver form
    // ----------------------------------------------------------------

    @GetMapping("/event/{eventId}")
    public String eventDetail(@PathVariable Long eventId,
                              HttpSession session,
                              Model model) {
        if (!isManager(session)) return "redirect:/auth/login";

        model.addAttribute("event", eventService.getEventDetail(eventId));
        model.addAttribute("eligibleDrivers", eventService.getEligibleDrivers());
        model.addAttribute("assignForm", new AssignDriverFormDto());
        model.addAttribute("statuses", TransportEventStatus.values());
        return "manager/event-detail";
    }

    // ----------------------------------------------------------------
    // Create event — show form
    // ----------------------------------------------------------------

    @GetMapping("/event/new")
    public String showCreateForm(HttpSession session, Model model) {
        if (!isManager(session)) return "redirect:/auth/login";

        model.addAttribute("form", new CreateTransportEventFormDto());
        model.addAttribute("shiftTypes", eventService.getActiveShiftTypes());
        return "manager/event-form";
    }

    // ----------------------------------------------------------------
    // Create event — handle POST
    // ----------------------------------------------------------------

    @PostMapping("/event/new")
    public String createEvent(@Valid @ModelAttribute("form") CreateTransportEventFormDto form,
                              BindingResult bindingResult,
                              HttpSession session,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        if (!isManager(session)) return "redirect:/auth/login";

        if (bindingResult.hasErrors()) {
            model.addAttribute("shiftTypes", eventService.getActiveShiftTypes());
            return "manager/event-form";
        }

        eventService.createEvent(form);
        redirectAttributes.addFlashAttribute("successMessage", "Transport event created successfully.");
        return "redirect:/manager/dashboard";
    }

    // ----------------------------------------------------------------
    // Assign driver
    // ----------------------------------------------------------------

    @PostMapping("/event/{eventId}/assign-driver")
    public String assignDriver(@PathVariable Long eventId,
                               @Valid @ModelAttribute("assignForm") AssignDriverFormDto form,
                               BindingResult bindingResult,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        if (!isManager(session)) return "redirect:/auth/login";

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please select a valid driver.");
            return "redirect:/manager/event/" + eventId;
        }

        eventService.assignDriver(eventId, form);
        redirectAttributes.addFlashAttribute("successMessage", "Driver assigned successfully.");
        return "redirect:/manager/event/" + eventId;
    }

    // ----------------------------------------------------------------
    // Change event status
    // ----------------------------------------------------------------

    @PostMapping("/event/{eventId}/status")
    public String changeStatus(@PathVariable Long eventId,
                               @RequestParam("status") TransportEventStatus status,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        if (!isManager(session)) return "redirect:/auth/login";

        eventService.updateEventStatus(eventId, status);
        redirectAttributes.addFlashAttribute("successMessage", "Event status updated to " + status + ".");
        return "redirect:/manager/event/" + eventId;
    }

    // ----------------------------------------------------------------
    // Helper
    // ----------------------------------------------------------------

    private boolean isManager(HttpSession session) {
        Boolean isManager = (Boolean) session.getAttribute("isManager");
        return Boolean.TRUE.equals(isManager);
    }
}
