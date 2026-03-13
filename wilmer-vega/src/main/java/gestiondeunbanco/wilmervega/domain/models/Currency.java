package gestiondeunbanco.wilmervega.domain.models;

public enum Currency {
    USD("US Dollar"),
    COP("Colombian Peso"),
    EUR("Euro");

    private final String description;

    Currency(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
