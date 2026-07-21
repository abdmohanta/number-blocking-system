package com.number_blocking_system;

import com.number_blocking_system.service.BlockingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BlockingInitializer {
    @Autowired
    private BlockingService blockingService;

    /**
     * Auto-cleanup on application startup
     * This ensures DB stays clean without needing a scheduler
     */
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        log.info("Application started - performing cleanup of expired blocks");
        blockingService.cleanupExpiredBlocks();
    }

}
