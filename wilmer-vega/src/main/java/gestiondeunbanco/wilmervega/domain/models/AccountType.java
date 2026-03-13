package gestiondeunbanco.wilmervega.domain.models;

public enum AccountType {
    SAVINGS("Savings"),
    CHECKING("Checking"),
    PERSONAL("Personal"),
    BUSINESS("Business");

    private final String description;

    AccountType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
