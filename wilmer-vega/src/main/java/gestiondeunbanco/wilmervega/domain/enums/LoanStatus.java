package gestiondeunbanco.wilmervega.domain.enums;

/**
 * Allowed flow:
 *   UNDER_REVIEW → APPROVED | REJECTED
 *   APPROVED     → DISBURSED
 *   DISBURSED    → DEFAULTED | PAID_OFF
 */
public enum LoanStatus {
    UNDER_REVIEW("Under Review"),
    APPROVED("Approved"),
    REJECTED("Rejected"),
    DISBURSED("Disbursed"),
    DEFAULTED("Defaulted"),
    PAID_OFF("Paid Off");

    private final String description;

    LoanStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
