package com.shimon.transport.entity;

import com.shimon.transport.enums.TransportEventStatus;
import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * A transport event represents a specific date + shift that requires
 * transportation coordination. Created by a manager; employees submit
 * requests against it.
 */
@Entity
@Table(name = "transport_events",
       uniqueConstraints = @UniqueConstraint(
           name = "uq_event_date_shift",
           columnNames = {"event_date", "shift_type_id"}
       ))
public class TransportEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_date", nullable = false)
    private LocalDate eventDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shift_type_id", nullable = false)
    private ShiftType shiftType;

    @Column(name = "transport_required", nullable = false)
    private boolean transportRequired = true;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "transport_event_status")
    @Type(PostgreSQLEnumType.class)
    private TransportEventStatus status = TransportEventStatus.OPEN;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_driver_id")
    private User assignedDriver;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @OneToMany(mappedBy = "transportEvent", fetch = FetchType.LAZY)
    private List<TransportRequest> requests = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // -- Constructors --

    public TransportEvent() {
    }

    // -- Getters and Setters --

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public ShiftType getShiftType() {
        return shiftType;
    }

    public void setShiftType(ShiftType shiftType) {
        this.shiftType = shiftType;
    }

    public boolean isTransportRequired() {
        return transportRequired;
    }

    public void setTransportRequired(boolean transportRequired) {
        this.transportRequired = transportRequired;
    }

    public TransportEventStatus getStatus() {
        return status;
    }

    public void setStatus(TransportEventStatus status) {
        this.status = status;
    }

    public User getAssignedDriver() {
        return assignedDriver;
    }

    public void setAssignedDriver(User assignedDriver) {
        this.assignedDriver = assignedDriver;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<TransportRequest> getRequests() {
        return requests;
    }

    public void setRequests(List<TransportRequest> requests) {
        this.requests = requests;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
