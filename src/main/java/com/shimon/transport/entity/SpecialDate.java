package com.shimon.transport.entity;

import com.shimon.transport.enums.SpecialDateType;
import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * A manually defined date with special transport relevance
 * (holiday, holiday eve, wartime, custom).
 */
@Entity
@Table(name = "special_dates")
public class SpecialDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "special_date", nullable = false, unique = true)
    private LocalDate specialDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "date_type", nullable = false, columnDefinition = "special_date_type")
    @Type(PostgreSQLEnumType.class)
    private SpecialDateType dateType;

    @Column(length = 200)
    private String description;

    @Column(name = "transport_relevant", nullable = false)
    private boolean transportRelevant = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // -- Constructors --

    public SpecialDate() {
    }

    // -- Getters and Setters --

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getSpecialDate() {
        return specialDate;
    }

    public void setSpecialDate(LocalDate specialDate) {
        this.specialDate = specialDate;
    }

    public SpecialDateType getDateType() {
        return dateType;
    }

    public void setDateType(SpecialDateType dateType) {
        this.dateType = dateType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isTransportRelevant() {
        return transportRelevant;
    }

    public void setTransportRelevant(boolean transportRelevant) {
        this.transportRelevant = transportRelevant;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
