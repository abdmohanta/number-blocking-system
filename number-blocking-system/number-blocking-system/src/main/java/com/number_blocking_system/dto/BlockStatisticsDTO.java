package com.number_blocking_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BlockStatisticsDTO {
    private Long activeBlocksCount;
    private LocalDateTime lastUpdated;
}
