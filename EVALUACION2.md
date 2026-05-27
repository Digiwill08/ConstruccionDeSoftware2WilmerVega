# EVALUACION 2 - ConstruccionDeSoftware2WilmerVega

## Informacion general
- Estudiante(s): Wilmer Vega (usuario GitHub: Digiwill08)
- Rama evaluada: origin/develop
- Commit evaluado: c64f137a76f0a78e0c49e2b5a67a7c6ebe4a3f9c
- Fecha: 2026-04-11
- Nota: La evaluación corresponde al estado del dominio en ese commit.

---

## Tabla de calificacion

| # | Criterio | Peso | Puntaje (1-5) | Parcial |
|---|---|---|---|---|
| 1 | Modelado de dominio | 20% | 4 | 0.80 |
| 2 | Modelado de puertos | 20% | 3 | 0.60 |
| 3 | Modelado de servicios de dominio | 20% | 2 | 0.40 |
| 4 | Enums y estados | 10% | 4 | 0.40 |
| 5 | Reglas de negocio criticas | 10% | 1 | 0.10 |
| 6 | Bitacora y trazabilidad | 5% | 3 | 0.15 |
| 7 | Estructura interna de dominio | 10% | 5 | 0.50 |
| 8 | Calidad tecnica base en domain | 5% | 4 | 0.20 |
| | **Total base** | | | **3.15** |

### Calculo
Nota base = (4*20 + 3*20 + 2*20 + 4*10 + 1*10 + 3*5 + 5*10 + 4*5) / 100 = 315 / 100 = **3.15**

---

## Penalizaciones aplicadas

Ninguna penalizacion mayor aplicable.

---

## Bonus aplicados

| Bonus | Valor | Motivo |
|---|---|---|
| Buen diseno de puertos por agregado | +0.2 | Puertos separados por agregado, interfaces limpias sin dependencias de framework |

Nota con bonus: 3.15 + 0.2 = **3.35**

---

## Nota final
**3.3 / 5.0**

---

## Hallazgos

### Criterio 1 - Modelado de dominio (4/5)
- Entidades completas: `AuditLog`, `BankAccount`, `BankingProduct`, `Client`, `CompanyClient`, `Loan`, `NaturalClient`, `SystemUser`, `Transfer`, `User`, `UserManager`.
- Enums bien definidos: `AccountStatus`, `AccountType`, `LoanStatus`, `LoanType`, `TransferStatus`, `UserStatus`, `SystemRole`.
- Jerarquia de clientes: `Client`, `NaturalClient`, `CompanyClient`.
- Falta: relaciones explicitas entre entidades (ej. `BankAccount` referenciando `Client`).
- Falta: `ProductCategory` como enum separado.

### Criterio 2 - Modelado de puertos (3/5)
- Puertos por agregado: `AuditLogPort`, `BankAccountPort`, `CompanyClientPort`, `LoanPort`, `NaturalClientPort`, `TransferPort`, `UserPort`.
- Sin dependencias de framework en las interfaces.
- **Problema:** Metodos de puertos son CRUD genericos (`findAll`, `findById`, `save`, `deleteById`) en lugar de firmas semanticas del negocio.
- Ejemplo `TransferPort`: no tiene `findPendingApprovalOlderThanMinutes(int)` ni `findPendingByCompany(Company)`.
- Los puertos no expresan las reglas del negocio en sus firmas.

### Criterio 3 - Servicios de dominio (2/5)
- Servicios presentes por entidad: `Create*`, `Delete*`, `Find*`, `Update*` para cada agregado.
- `CreateAuditLog`, `CreateBankAccount`, `CreateCompanyClient`, `CreateLoan`, `CreateNaturalClient`, `CreateTransfer`, `CreateUser`.
- **Problema critico:** `CreateTransfer` solo valida que la transferencia no sea `null` y la guarda — no hay validacion de monto, no hay validacion de cuentas operativas, no hay logica de aprobacion empresarial.
- No hay: `ApproveLoanService`, `RejectLoanService`, `DisburseLoanService`, `ApproveTransferService`, `ExpirePendingTransfersService`.
- Los servicios son envoltorios delgados CRUD sin logica de negocio.

### Criterio 4 - Enums y estados (4/5)
- Enums cubriendo: `AccountStatus`, `AccountType`, `LoanStatus`, `LoanType`, `TransferStatus`, `UserStatus`, `SystemRole`.
- Buena cobertura de los estados criticos.
- Falta: `Currency/Moneda`.

### Criterio 5 - Reglas de negocio criticas (1/5)
- `CreateTransfer`: solo valida `transfer != null`. Sin monto > 0, sin validacion de saldo, sin logica de aprobacion empresarial.
- No hay ciclo de prestamo implementado.
- No hay vencimiento de transferencias.
- Las reglas de negocio criticas estan ausentes en el dominio.

### Criterio 6 - Bitacora y trazabilidad (3/5)
- `AuditLog` como entidad de dominio.
- `AuditLogPort` como interfaz de salida.
- `CreateAuditLog` como servicio.
- Falta: los servicios transaccionales no llaman a `CreateAuditLog` al ejecutar operaciones.

### Criterio 7 - Estructura interna de dominio (5/5)
- Arquitectura hexagonal bien aplicada: `domain/models/`, `domain/ports/`, `domain/services/`.
- Sin dependencias de Spring en la capa domain (servicios son POJOs puros).
- Separacion clara entre domain, application (adapters/usecases) e infrastructure.
- El README documenta el proyecto y la arquitectura de forma excelente.

### Criterio 8 - Calidad tecnica (4/5)
- Nomenclatura en ingles consistente.
- Servicios son POJOs sin anotaciones de framework (positivo).
- Codigo claro y legible.
- La debilidad principal es la falta de logica de negocio, no la calidad del codigo en si.

---

## Recomendaciones
1. Agregar logica de negocio en `CreateTransfer`: validar monto > 0, cuentas existentes y activas, umbral empresarial que trigger flujo de aprobacion.
2. Implementar `ApproveLoanService`, `RejectLoanService`, `DisburseLoanService` con ciclo de estado completo.
3. Implementar `ApproveTransferService`, `ExpirePendingTransfersService` con logica de 60 minutos.
4. Enriquecer firmas de puertos para expresar semantica de negocio.
5. Lograr que cada servicio transaccional registre en bitacora via `AuditLogPort`.
