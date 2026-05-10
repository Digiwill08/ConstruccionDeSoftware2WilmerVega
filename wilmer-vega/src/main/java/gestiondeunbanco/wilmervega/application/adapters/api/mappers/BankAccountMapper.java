package gestiondeunbanco.wilmervega.application.adapters.api.mappers;

import gestiondeunbanco.wilmervega.application.adapters.api.dto.BankAccountRequest;
import gestiondeunbanco.wilmervega.domain.models.BankAccount;
import gestiondeunbanco.wilmervega.domain.models.NaturalClient;
import gestiondeunbanco.wilmervega.domain.models.AccountStatus;
import gestiondeunbanco.wilmervega.domain.models.AccountType;
import gestiondeunbanco.wilmervega.domain.models.Currency;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class BankAccountMapper {

    /**
     * Convierte un BankAccountRequest en el modelo de dominio BankAccount.
     * Protege campos sensibles y de auditoría: no asigna IDs, ni sobrescribe
     * fechas de apertura ni otros metadatos que deba controlar el dominio/servicio.
     */
    public BankAccount toModel(BankAccountRequest request) {
        BankAccount model = new BankAccount();

        model.setAccountNumber(request.getAccountNumber());
        if (request.getAccountType() != null) {
            model.setAccountType(AccountType.valueOf(request.getAccountType().trim().toUpperCase()));
        }
        if (request.getCurrency() != null) {
            model.setCurrency(Currency.valueOf(request.getCurrency().trim().toUpperCase()));
        }

        // No setear ID ni otros campos sensibles desde el request.
        // La fecha de apertura se establece aquí a la fecha actual (creación),
        // esto puede desplazarse al servicio si se requiere más control.
        model.setOpeningDate(LocalDate.now());

        // Usar el initialBalance del request como saldo inicial.
        BigDecimal initial = request.getInitialBalance();
        model.setCurrentBalance(initial != null ? initial : BigDecimal.ZERO);

        model.setAccountStatus(AccountStatus.ACTIVE);

        NaturalClient holder = new NaturalClient();
        holder.setId(request.getHolderId());
        model.setHolder(holder);

        return model;
    }
}
