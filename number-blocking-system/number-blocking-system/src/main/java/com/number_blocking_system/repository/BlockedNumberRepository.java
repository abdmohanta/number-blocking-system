package com.number_blocking_system.repository;

import com.number_blocking_system.entity.BlockedNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlockedNumberRepository extends JpaRepository<BlockedNumber, Long> {

    // Find active blocks for a number (with auto-cleanup)
    @Query(value = """
        UPDATE blocked_numbers 
        SET is_active = false 
        WHERE is_active = true 
        AND blocked_at + (block_duration_days || ' days')::INTERVAL < NOW();
        
        SELECT * FROM blocked_numbers 
        WHERE phone_number = :phoneNumber 
        AND is_active = true 
        AND blocked_at + (block_duration_days || ' days')::INTERVAL > NOW()
        LIMIT 1
    """, nativeQuery = true)
    Optional<BlockedNumber> findActiveBlock(@Param("phoneNumber") String phoneNumber);

    // Find all expired active blocks
    @Query(value = """
        SELECT * FROM blocked_numbers 
        WHERE is_active = true 
        AND blocked_at + (block_duration_days || ' days')::INTERVAL < NOW()
    """, nativeQuery = true)
    List<BlockedNumber> findExpiredBlocks();

    // Count active blocks
    @Query(value = """
        SELECT COUNT(*) FROM blocked_numbers 
        WHERE is_active = true 
        AND blocked_at + (block_duration_days || ' days')::INTERVAL > NOW()
    """, nativeQuery = true)
    Long countActiveBlocks();

    // Find by phone number (any status)
    Optional<BlockedNumber> findByPhoneNumber(String phoneNumber);
}