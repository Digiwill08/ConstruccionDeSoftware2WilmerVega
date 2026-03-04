package gestiondeunbanco.wilmervega.domain.entities;

import gestiondeunbanco.wilmervega.domain.enums.OperationType;
import gestiondeunbanco.wilmervega.domain.enums.SystemRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Audit log of all system operations.
 * This entity is designed to be stored in a NoSQL database
 * (like MongoDB) due to the variability of detail data.
 * 
 * Note: Does not use JPA annotations since it's stored in NoSQL.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OperationLog {

    /**
     * Unique log record identifier.
     */
    private String logId;

    /**
     * Type of operation performed.
     * Examples: ACCOUNT_OPENING, TRANSFER, LOAN_APPROVAL, etc.
     */
    private OperationType operationType;

    /**
     * Exact moment of the operation.
     */
    private LocalDateTime operationDateTime;

    /**
     * ID of the user who executed the action.
     */
    private Long userId;

    /**
     * User's role at the time of the operation.
     */
    private SystemRole userRole;

    /**
     * Reference to the main product affected (Account, Loan, Transfer).
     */
    private String affectedProductId;

    /**
     * Variable data depending on the operation type.
     * Content examples:
     * - For TRANSFER_EXECUTED: amount, sourceBalanceBefore, sourceBalanceAfter, etc.
     * - For LOAN_APPROVAL: approvedAmount, interestRate, previousStatus, newStatus, etc.
     * - For TRANSFER_EXPIRED: expirationReason, expirationDateTime, etc.
     */
    private Map<String, Object> detailData;

    /**
     * Additional description of the operation.
     */
    private String description;

    /**
     * IP address from where the operation was performed (optional).
     */
    private String ipAddress;
}
