package com.number_blocking_system.controller;

import com.number_blocking_system.dto.BlockDetailsDTO;
import com.number_blocking_system.dto.BlockRequestDTO;
import com.number_blocking_system.dto.BlockResponseDTO;
import com.number_blocking_system.dto.BlockStatisticsDTO;
import com.number_blocking_system.entity.BlockedNumber;
import com.number_blocking_system.service.BlockingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/blocks")
@Slf4j
public class BlockingController {

    @Autowired
    private BlockingService blockingService;

    /**
     * Check if number is blocked
     * GET /api/v1/blocks/check?phoneNumber=1234567890
     */
    @GetMapping("/check")
    public ResponseEntity<BlockResponseDTO> checkNumber(@RequestParam String phoneNumber) {
        boolean isBlocked = blockingService.isNumberBlocked(phoneNumber);
        BlockDetailsDTO details = blockingService.getBlockDetails(phoneNumber);

        return ResponseEntity.ok(new BlockResponseDTO(isBlocked, details));
    }

    /**
     * Block a number
     * POST /api/v1/blocks
     * {
     *   "phoneNumber": "1234567890",
     *   "blockDurationDays": 1,
     *   "reason": "Suspicious activity"
     * }
     */
    @PostMapping
    public ResponseEntity<BlockedNumber> blockNumber(@RequestBody BlockRequestDTO request) {
        BlockedNumber blocked = blockingService.blockNumber(
                request.getPhoneNumber(),
                request.getBlockDurationDays(),
                request.getReason()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(blocked);
    }

    /**
     * Unblock a number
     * DELETE /api/v1/blocks?phoneNumber=1234567890
     */
    @DeleteMapping
    public ResponseEntity<Void> unblockNumber(@RequestParam String phoneNumber) {
        blockingService.unblockNumber(phoneNumber);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get block details
     * GET /api/v1/blocks/details?phoneNumber=1234567890
     */
    @GetMapping("/details")
    public ResponseEntity<BlockDetailsDTO> getDetails(@RequestParam String phoneNumber) {
        BlockDetailsDTO details = blockingService.getBlockDetails(phoneNumber);
        if (details == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(details);
    }

    /**
     * Get statistics
     * GET /api/v1/blocks/stats
     */
    @GetMapping("/stats")
    public ResponseEntity<BlockStatisticsDTO> getStats() {
        return ResponseEntity.ok(blockingService.getStatistics());
    }

    /**
     * Manual cleanup (can be called on demand or during startup)
     * POST /api/v1/blocks/cleanup
     */
    @PostMapping("/cleanup")
    public ResponseEntity<String> cleanup() {
        blockingService.cleanupExpiredBlocks();
        return ResponseEntity.ok("Cleanup completed");
    }
}