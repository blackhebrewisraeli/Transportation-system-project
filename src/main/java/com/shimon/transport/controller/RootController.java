package com.shimon.transport.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Redirects the bare root URL "/" to the login page.
 * Prevents a 500 "No static resource" error when users visit the domain root.
 */
@Controller
public class RootController {

    @GetMapping("/")
    public String root() {
        return "redirect:/auth/login";
    }
}
