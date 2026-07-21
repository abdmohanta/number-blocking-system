package com.number_blocking_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BlockResponseDTO {
    private boolean isBlocked;
    private BlockDetailsDTO details;
}