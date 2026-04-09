package com.shimon.transport.controller;

import com.shimon.transport.dto.EditNameFormDto;
import com.shimon.transport.entity.User;
import com.shimon.transport.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Admin-only controller for user management.
 * All endpoints are protected by isSystemAdmin check.
 *
 * Currently supports: view all users, edit any user's full name.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    // ----------------------------------------------------------------
    // User list
    // ----------------------------------------------------------------

    @GetMapping("/users")
    public String listUsers(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/auth/login";

        model.addAttribute("users", userService.getAllUsers());
        return "admin/users";
    }

    // ----------------------------------------------------------------
    // Edit name — show form
    // ----------------------------------------------------------------

    @GetMapping("/users/{userId}/edit-name")
    public String showEditName(@PathVariable Long userId,
                               HttpSession session,
                               Model model) {
        if (!isAdmin(session)) return "redirect:/auth/login";

        User target = userService.getById(userId);

        EditNameFormDto form = new EditNameFormDto();
        form.setFullName(target.getFullName());

        model.addAttribute("target", target);
        model.addAttribute("form", form);
        return "admin/edit-name";
    }

    // ----------------------------------------------------------------
    // Edit name — handle POST
    // ----------------------------------------------------------------

    @PostMapping("/users/{userId}/edit-name")
    public String saveEditName(@PathVariable Long userId,
                               @Valid @ModelAttribute("form") EditNameFormDto form,
                               BindingResult bindingResult,
                               HttpSession session,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) return "redirect:/auth/login";

        if (bindingResult.hasErrors()) {
            User target = userService.getById(userId);
            model.addAttribute("target", target);
            return "admin/edit-name";
        }

        userService.updateUserName(userId, form.getFullName().trim());

        // If the admin renamed themselves, refresh the session display name
        Long adminId = (Long) session.getAttribute("userId");
        if (adminId != null && adminId.equals(userId)) {
            session.setAttribute("userName", form.getFullName().trim());
        }

        redirectAttributes.addFlashAttribute("successMessage",
                "Name updated successfully for user #" + userId + ".");
        return "redirect:/admin/users";
    }

    // ----------------------------------------------------------------
    // Helper
    // ----------------------------------------------------------------

    private boolean isAdmin(HttpSession session) {
        return Boolean.TRUE.equals(session.getAttribute("isSystemAdmin"));
    }
}
