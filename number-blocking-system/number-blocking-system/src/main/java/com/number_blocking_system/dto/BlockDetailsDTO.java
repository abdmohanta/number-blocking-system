package com.number_blocking_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BlockDetailsDTO {
    private Long id;
    private String phoneNumber;
    private String blockReason;
    private LocalDateTime blockedAt;
    private Integer blockDurationDays;
    private Long remainingDays;
    private Boolean isActive;
}
