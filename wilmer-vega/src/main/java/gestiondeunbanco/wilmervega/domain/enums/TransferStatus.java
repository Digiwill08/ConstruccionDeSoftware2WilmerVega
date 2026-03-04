package gestiondeunbanco.wilmervega.domain.enums;

/**
 * Low amount flow:  PENDING → EXECUTED
 * High amount flow: AWAITING_APPROVAL → APPROVED → EXECUTED
 *                   AWAITING_APPROVAL → REJECTED | EXPIRED (60 min)
 */
public enum TransferStatus {
    PENDING("Pending"),
    AWAITING_APPROVAL("Awaiting Approval"),
    APPROVED("Approved"),
    EXECUTED("Executed"),
    REJECTED("Rejected"),
    EXPIRED("Expired");

    private final String description;

    TransferStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
