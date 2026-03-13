package gestiondeunbanco.wilmervega.domain.models;

public enum OperationType {
    ACCOUNT_OPENING("Bank account opening"),
    ACCOUNT_CLOSURE("Bank account closure"),
    DEPOSIT("Account deposit"),
    WITHDRAWAL("Account withdrawal"),
    TRANSFER_EXECUTED("Transfer executed"),
    TRANSFER_REJECTED("Transfer rejected"),
    TRANSFER_EXPIRED("Transfer expired due to lack of approval"),
    LOAN_REQUEST("Loan request created"),
    LOAN_APPROVAL("Loan approved by analyst"),
    LOAN_REJECTION("Loan rejected by analyst"),
    LOAN_DISBURSEMENT("Loan disbursement executed"),
    USER_CREATION("New user creation"),
    USER_BLOCKING("User blocked"),
    LOGIN("Login"),
    LOGOUT("Logout");

    private final String description;

    OperationType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
