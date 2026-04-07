package com.shimon.transport.entity;

import com.shimon.transport.enums.UserType;
import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Central user entity. Covers employees, volunteer drivers, retirees, and staff.
 * Role/permission logic is driven by boolean flags, not a separate roles table.
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(name = "phone_number", nullable = false, unique = true, length = 20)
    private String phoneNumber;

    @Column(length = 150)
    private String email;

    @Column(name = "address_text", columnDefinition = "TEXT")
    private String addressText;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false, columnDefinition = "user_type")
    @Type(PostgreSQLEnumType.class)
    private UserType userType = UserType.EMPLOYEE;

    @Column(name = "can_request_transport", nullable = false)
    private boolean canRequestTransport = true;

    @Column(name = "can_drive", nullable = false)
    private boolean canDrive = false;

    @Column(name = "is_shift_manager", nullable = false)
    private boolean shiftManager = false;

    @Column(name = "is_system_admin", nullable = false)
    private boolean systemAdmin = false;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // -- Constructors --

    public User() {
    }

    // -- Getters and Setters --

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddressText() {
        return addressText;
    }

    public void setAddressText(String addressText) {
        this.addressText = addressText;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public boolean isCanRequestTransport() {
        return canRequestTransport;
    }

    public void setCanRequestTransport(boolean canRequestTransport) {
        this.canRequestTransport = canRequestTransport;
    }

    public boolean isCanDrive() {
        return canDrive;
    }

    public void setCanDrive(boolean canDrive) {
        this.canDrive = canDrive;
    }

    public boolean isShiftManager() {
        return shiftManager;
    }

    public void setShiftManager(boolean shiftManager) {
        this.shiftManager = shiftManager;
    }

    public boolean isSystemAdmin() {
        return systemAdmin;
    }

    public void setSystemAdmin(boolean systemAdmin) {
        this.systemAdmin = systemAdmin;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
