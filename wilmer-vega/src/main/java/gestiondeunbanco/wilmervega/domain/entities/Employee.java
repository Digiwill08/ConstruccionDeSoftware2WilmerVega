package gestiondeunbanco.wilmervega.domain.entities;

import gestiondeunbanco.wilmervega.domain.enums.SystemRole;
import gestiondeunbanco.wilmervega.domain.enums.UserStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

/**
 * Employee - person who works at the bank
 */
@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    private String employeeCode;

    private String fullName;

    private String identificationNumber;

    private String email;

    private String phone;

    private LocalDate hireDate;

    private String department;

    private String position;

    private SystemRole role;

    private UserStatus status;

    // Check if employee is working
    public boolean isActive() {
        return status == UserStatus.ACTIVE;
    }

    // Check if can approve loans
    public boolean canApproveLoans() {
        return role == SystemRole.INTERNAL_ANALYST || 
               role == SystemRole.COMPANY_SUPERVISOR;
    }

    // Check if can approve transfers
    public boolean canApproveTransfers() {
        return role == SystemRole.COMPANY_SUPERVISOR || 
               role == SystemRole.COMPANY_EMPLOYEE;
    }

    // Calculate years working
    public int getYearsOfService() {
        if (hireDate == null) {
            return 0;
        }
        return LocalDate.now().getYear() - hireDate.getYear();
    }
}
