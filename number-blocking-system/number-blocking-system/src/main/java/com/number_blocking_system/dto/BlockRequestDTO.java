package com.number_blocking_system.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BlockRequestDTO {
    private String phoneNumber;
    private Integer blockDurationDays;
    private String reason;
}
