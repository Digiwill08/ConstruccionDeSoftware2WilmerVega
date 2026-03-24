package gestiondeunbanco.wilmervega.domain.models;

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
