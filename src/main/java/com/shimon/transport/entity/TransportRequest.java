package com.shimon.transport.entity;

import com.shimon.transport.enums.RequestDirection;
import com.shimon.transport.enums.RequestStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * An employee's request to be included in a specific transport event.
 * Each user may have at most one request per transport event.
 */
@Entity
@Table(name = "transport_requests",
       uniqueConstraints = @UniqueConstraint(
           name = "uq_user_event",
           columnNames = {"user_id", "transport_event_id"}
       ))
public class TransportRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transport_event_id", nullable = false)
    private TransportEvent transportEvent;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RequestDirection direction;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RequestStatus status = RequestStatus.PENDING;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // -- Constructors --

    public TransportRequest() {
    }

    // -- Getters and Setters --

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TransportEvent getTransportEvent() {
        return transportEvent;
    }

    public void setTransportEvent(TransportEvent transportEvent) {
        this.transportEvent = transportEvent;
    }

    public RequestDirection getDirection() {
        return direction;
    }

    public void setDirection(RequestDirection direction) {
        this.direction = direction;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
