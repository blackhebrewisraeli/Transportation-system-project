package com.shimon.transport.exception;

/**
 * Thrown when a business rule is violated.
 * Examples: duplicate request, inactive user, event not open, etc.
 */
public class BusinessRuleException extends RuntimeException {

    public BusinessRuleException(String message) {
        super(message);
    }
}
