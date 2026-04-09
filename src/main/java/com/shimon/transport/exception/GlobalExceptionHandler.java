package com.shimon.transport.exception;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Global exception handler — renders a modal-style error page with the HTTP status code
 * and a descriptive message explaining what went wrong.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(ResourceNotFoundException ex, Model model) {
        model.addAttribute("errorCode",    "404");
        model.addAttribute("errorTitle",   "Not Found");
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/error-modal";
    }

    @ExceptionHandler(BusinessRuleException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBusinessRule(BusinessRuleException ex, Model model) {
        model.addAttribute("errorCode",    "400");
        model.addAttribute("errorTitle",   "Invalid Request");
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/error-modal";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGeneral(Exception ex, Model model) {
        String detail = (ex.getMessage() != null && !ex.getMessage().isBlank())
                ? ex.getMessage()
                : ex.getClass().getSimpleName();
        model.addAttribute("errorCode",    "500");
        model.addAttribute("errorTitle",   "Internal Server Error");
        model.addAttribute("errorMessage", detail);
        return "error/error-modal";
    }
}
