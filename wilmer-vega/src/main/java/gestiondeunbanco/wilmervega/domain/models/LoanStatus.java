package gestiondeunbanco.wilmervega.domain.models;

/**
 * Allowed flow:
 *   UNDER_REVIEW -> APPROVED | REJECTED
 *   APPROVED     -> DISBURSED
 *   DISBURSED    -> OVERDUE | CANCELLED
 */
public enum LoanStatus {
    UNDER_REVIEW("Under review"),
    APPROVED("Approved"),
    REJECTED("Rejected"),
    DISBURSED("Disbursed"),
    OVERDUE("Overdue"),
    CANCELLED("Cancelled");

    private final String description;

    LoanStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
