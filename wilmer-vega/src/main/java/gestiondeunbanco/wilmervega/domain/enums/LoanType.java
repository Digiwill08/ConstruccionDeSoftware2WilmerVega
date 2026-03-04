package gestiondeunbanco.wilmervega.domain.enums;

public enum LoanType {
    CONSUMER("Consumer"),
    VEHICLE("Vehicle"),
    MORTGAGE("Mortgage"),
    BUSINESS("Business");

    private final String description;

    LoanType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
