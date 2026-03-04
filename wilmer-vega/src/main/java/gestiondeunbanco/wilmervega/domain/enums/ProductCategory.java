package gestiondeunbanco.wilmervega.domain.enums;

public enum ProductCategory {
    ACCOUNTS("Accounts"),
    LOANS("Loans"),
    SERVICES("Services");

    private final String description;

    ProductCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
