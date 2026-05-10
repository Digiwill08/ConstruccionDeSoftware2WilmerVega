package gestiondeunbanco.wilmervega;

import gestiondeunbanco.wilmervega.application.adapters.api.dto.BankAccountRequest;
import org.junit.jupiter.api.Test;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.math.BigDecimal;
import java.util.Set;

import jakarta.validation.ConstraintViolation;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class BankAccountRequestValidationTest {

    @Test
    void invalidRequestShouldHaveConstraintViolations() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        // Create request with missing/invalid fields
        BankAccountRequest req = new BankAccountRequest();
        req.setAccountNumber(null);
        req.setAccountType(null);
        req.setCurrency(null);
        req.setHolderId(null);
        req.setInitialBalance(null);

        Set<ConstraintViolation<BankAccountRequest>> violations = validator.validate(req);

        assertFalse(violations.isEmpty(), "Expected validation violations for invalid request");
    }
}
