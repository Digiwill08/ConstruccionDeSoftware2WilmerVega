package gestiondeunbanco.wilmervega.domain.models;

public enum AccountStatus {
    ACTIVE("Active"),
    BLOCKED("Blocked"),
    CANCELLED("Cancelled");

    private final String description;

    AccountStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
