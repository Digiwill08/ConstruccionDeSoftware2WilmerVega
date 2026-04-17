package gestiondeunbanco.wilmervega.domain.models;

public enum SystemRole {
    ADMINISTRATOR("Platform Administrator"),
    NATURAL_CLIENT("Natural Person Client"),
    COMPANY_CLIENT("Company Client"),
    TELLER_EMPLOYEE("Teller Employee"),
    COMMERCIAL_EMPLOYEE("Commercial Employee"),
    COMPANY_EMPLOYEE("Company Employee"),
    COMPANY_SUPERVISOR("Company Supervisor"),
    INTERNAL_ANALYST("Internal Bank Analyst");

    private final String description;

    SystemRole(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
