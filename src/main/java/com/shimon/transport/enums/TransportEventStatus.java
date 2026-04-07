package com.shimon.transport.enums;

/**
 * Status of a transport event (a date + shift requiring coordination).
 * OPEN = accepting requests, LOCKED = finalized, CANCELLED = not happening, COMPLETED = done.
 */
public enum TransportEventStatus {
    OPEN,
    LOCKED,
    CANCELLED,
    COMPLETED
}
