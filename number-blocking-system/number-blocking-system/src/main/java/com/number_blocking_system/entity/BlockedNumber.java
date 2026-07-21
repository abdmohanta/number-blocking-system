package com.number_blocking_system.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "blocked_numbers")
public class BlockedNumber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phone_number", unique = true, nullable = false)
    private String phoneNumber;

    @Column(name = "blocked_at", nullable = false)
    private LocalDateTime blockedAt;

    @Column(name = "block_duration_days", nullable = false)
    private Integer blockDurationDays;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "block_reason")
    private String blockReason;

    // Constructor
    public BlockedNumber(String phoneNumber, Integer blockDurationDays, String blockReason) {
        this.phoneNumber = phoneNumber;
        this.blockDurationDays = blockDurationDays;
        this.blockReason = blockReason;
        this.blockedAt = LocalDateTime.now();
        this.isActive = true;
    }

    public BlockedNumber() {}

    // Check if block has expired (without saving to DB yet)
    @Transient
    public boolean isExpired() {
        LocalDateTime expiryTime = blockedAt.plusDays(blockDurationDays);
        return LocalDateTime.now().isAfter(expiryTime);
    }

    // Get remaining days
    @Transient
    public long getRemainingDays() {
        LocalDateTime expiryTime = blockedAt.plusDays(blockDurationDays);
        return Duration.between(LocalDateTime.now(), expiryTime).toDays();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public String getPhoneNumber() { return phoneNumber; }
    public LocalDateTime getBlockedAt() { return blockedAt; }
    public Integer getBlockDurationDays() { return blockDurationDays; }
    public Boolean getIsActive() { return isActive; }
    public String getBlockReason() { return blockReason; }

    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setBlockDurationDays(Integer blockDurationDays) { this.blockDurationDays = blockDurationDays; }
    public void setBlockReason(String blockReason) { this.blockReason = blockReason; }
}