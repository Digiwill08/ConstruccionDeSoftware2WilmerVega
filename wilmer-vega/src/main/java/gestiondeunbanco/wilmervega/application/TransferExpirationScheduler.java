package gestiondeunbanco.wilmervega.application;

import gestiondeunbanco.wilmervega.domain.services.ExpireTransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduled job that runs every 60 seconds to expire transfers
 * that have been in AWAITING_APPROVAL status for more than 60 minutes.
 */
@Component
@RequiredArgsConstructor
public class TransferExpirationScheduler {

    private final ExpireTransferService expireTransferService;

    /**
     * Runs every 60 seconds (60000 ms).
     * Finds all AWAITING_APPROVAL transfers older than 60 minutes and marks them as EXPIRED.
     */
    @Scheduled(fixedDelay = 60000)
    public void expireOldTransfers() {
        expireTransferService.expireAll();
    }
}
