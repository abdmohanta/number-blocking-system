package com.number_blocking_system.service;

import com.number_blocking_system.dto.BlockDetailsDTO;
import com.number_blocking_system.dto.BlockStatisticsDTO;
import com.number_blocking_system.entity.BlockedNumber;
import com.number_blocking_system.repository.BlockedNumberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class BlockingService {

    @Autowired
    private BlockedNumberRepository blockedNumberRepository;

    /**
     * Check if a number is currently blocked
     * Automatically unblocks if expiry time has passed
     */
    public boolean isNumberBlocked(String phoneNumber) {
        //log.info("Checking if number is blocked: {}", phoneNumber);

        Optional<BlockedNumber> blocked = blockedNumberRepository.findActiveBlock(phoneNumber);

        if (blocked.isEmpty()) {
            //log.debug("Number {} is not blocked", phoneNumber);
            return false;
        }

        BlockedNumber blockRecord = blocked.get();

        // Double-check expiry (in case of edge cases)
        if (blockRecord.isExpired()) {
            //log.info("Block for {} has expired, marking inactive", phoneNumber);
            blockRecord.setIsActive(false);
            blockedNumberRepository.save(blockRecord);
            return false;
        }

        long remainingDays = blockRecord.getRemainingDays();
        //log.info("Number {} is blocked. Remaining days: {}", phoneNumber, remainingDays);
        return true;
    }

    /**
     * Block a number for specified days
     */
    public BlockedNumber blockNumber(String phoneNumber, Integer durationDays, String reason) {
        //log.info("Blocking number: {} for {} days. Reason: {}", phoneNumber, durationDays, reason);

        // Remove any existing blocks
        Optional<BlockedNumber> existing = blockedNumberRepository.findByPhoneNumber(phoneNumber);
        if (existing.isPresent()) {
            existing.get().setIsActive(false);
            blockedNumberRepository.save(existing.get());
        }

        // Create new block
        BlockedNumber newBlock = new BlockedNumber(phoneNumber, durationDays, reason);
        BlockedNumber saved = blockedNumberRepository.save(newBlock);

        //log.info("Number {} blocked successfully. Block ID: {}", phoneNumber, saved.getId());
        return saved;
    }

    /**
     * Manually unblock a number
     */
    public void unblockNumber(String phoneNumber) {
        //log.info("Manually unblocking number: {}", phoneNumber);

        Optional<BlockedNumber> blocked = blockedNumberRepository.findByPhoneNumber(phoneNumber);
        if (blocked.isPresent() && blocked.get().getIsActive()) {
            blocked.get().setIsActive(false);
            blockedNumberRepository.save(blocked.get());
           // log.info("Number {} unblocked successfully", phoneNumber);
        } else {
           // log.warn("No active block found for number: {}", phoneNumber);
        }
    }

    /**
     * Get block details (if exists)
     */
    public BlockDetailsDTO getBlockDetails(String phoneNumber) {
        Optional<BlockedNumber> blocked = blockedNumberRepository.findActiveBlock(phoneNumber);

        if (blocked.isEmpty()) {
            return null;
        }

        BlockedNumber block = blocked.get();
        return new BlockDetailsDTO(
                block.getId(),
                block.getPhoneNumber(),
                block.getBlockReason(),
                block.getBlockedAt(),
                block.getBlockDurationDays(),
                block.getRemainingDays(),
                block.getIsActive()
        );
    }

    /**
     * Cleanup expired blocks (call during low traffic or startup)
     * This keeps DB clean without a scheduler
     */
    @Transactional
    public void cleanupExpiredBlocks() {
       // log.info("Starting cleanup of expired blocks...");

        List<BlockedNumber> expiredBlocks = blockedNumberRepository.findExpiredBlocks();

        if (expiredBlocks.isEmpty()) {
            //log.debug("No expired blocks to clean");
            return;
        }

        expiredBlocks.forEach(block -> block.setIsActive(false));
        blockedNumberRepository.saveAll(expiredBlocks);

        //log.info("Cleaned up {} expired blocks", expiredBlocks.size());
    }

    /**
     * Get statistics
     */
    public BlockStatisticsDTO getStatistics() {
        Long activeBlocksCount = blockedNumberRepository.countActiveBlocks();

        return new BlockStatisticsDTO(
                activeBlocksCount,
                LocalDateTime.now()
        );
    }
}