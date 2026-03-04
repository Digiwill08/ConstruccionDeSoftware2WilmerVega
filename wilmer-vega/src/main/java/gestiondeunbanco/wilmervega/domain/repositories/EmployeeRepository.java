package gestiondeunbanco.wilmervega.domain.repositories;

import gestiondeunbanco.wilmervega.domain.entities.Employee;
import gestiondeunbanco.wilmervega.domain.enums.SystemRole;
import gestiondeunbanco.wilmervega.domain.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Domain repository for Employee entity.
 * Defines persistence operations for bank employees.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    
    Optional<Employee> findByEmployeeCode(String employeeCode);
    
    Optional<Employee> findByIdentificationNumber(String identificationNumber);
    
    Optional<Employee> findByEmail(String email);
    
    List<Employee> findByRole(SystemRole role);
    
    List<Employee> findByStatus(UserStatus status);
    
    List<Employee> findByDepartment(String department);
    
    boolean existsByEmployeeCode(String employeeCode);
    
    boolean existsByIdentificationNumber(String identificationNumber);
}
