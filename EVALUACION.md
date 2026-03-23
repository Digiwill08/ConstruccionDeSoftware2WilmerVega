# EVALUACIÓN - ConstruccionDeSoftware2WilmerVega

## Información General
- **Estudiante(s):** Wilmer Vega (Digiwill08 / WILMER)
- **Rama evaluada:** main (checkout a develop falló por filenames largos; modelos están en main)
- **Fecha de evaluación:** 2026-03-23

---

## Tabla de Calificación

| # | Criterio | Peso | Puntaje (1–5) | Nota ponderada |
|---|---|---|---|---|
| 1 | Modelado de dominio | 25% | 5 | 1.25 |
| 2 | Relaciones entre entidades | 15% | 3 | 0.45 |
| 3 | Uso de Enums | 15% | 5 | 0.75 |
| 4 | Manejo de estados | 5% | 5 | 0.25 |
| 5 | Tipos de datos | 5% | 5 | 0.25 |
| 6 | Separación Usuario vs Cliente | 10% | 4 | 0.40 |
| 7 | Bitácora | 5% | 3 | 0.15 |
| 8 | Reglas básicas de negocio | 5% | 4 | 0.20 |
| 9 | Estructura del proyecto | 10% | 5 | 0.50 |
| 10 | Repositorio | 10% | 2 | 0.20 |
| **TOTAL** | | **100%** | | **4.40** |

## Penalizaciones
- Ninguna (código en inglés, nombres correctos).

## Bonus
- Sin herencia bonus (`CompanyClient extends NaturalClient` es semánticamente incorrecto).
- +2: Código limpio y organizado → +0.2
- +1: Nombres claros y consistentes → +0.1
- **Total: +0.3**

## Nota Final: 4.7 / 5.0

---

## Análisis por Criterio

### 1. Modelado de dominio — 5/5
Entidades completas: `BankAccount`, `Loan`, `Transfer`, `AuditLog`, `NaturalClient`, `CompanyClient`, `Person`, `User`, `BankingProduct`, `UserManager`. Además incluye `SystemUser`. Todas las entidades del dominio bancario están presentes con buena definición.

### 2. Relaciones entre entidades — 3/5
`BankAccount` tiene dos referencias separadas: `naturalClientHolder` y `companyClientHolder`, en lugar de una referencia polimórfica a un `Client` base. `User` solo se relaciona con `NaturalClient` (no con `CompanyClient`). Estas decisiones de diseño generan inconsistencias en el modelo dado que no hay una clase `Client` base abstracta unificada.

### 3. Uso de Enums — 5/5
Enums completos: `AccountStatus`, `AccountType`, `Currency`, `LoanStatus`, `LoanType`, `OperationType`, `ProductCategory`, `SystemRole`, `TransferStatus`, `UserStatus`. Todos los catálogos están modelados correctamente.

### 4. Manejo de estados — 5/5
Todos los estados usan enums correctamente tipados en todas las entidades.

### 5. Tipos de datos — 5/5
`BankAccount` usa `BigDecimal currentBalance` ✓ y `java.sql.Date` (aceptable, aunque se prefiere `java.time.LocalDate`). El uso de `BigDecimal` es correcto para montos financieros.

### 6. Separación Usuario vs Cliente — 4/5
`User` extiende `UserManager` y tiene una referencia a `NaturalClient relatedClient` ✓. Los clientes (`NaturalClient`, `CompanyClient`) son entidades separadas del `User`. Sin embargo, `User.relatedClient` solo referencia `NaturalClient`, no `CompanyClient`. Clientes de empresa quedan sin referencia desde `User`.

### 7. Bitácora — 3/5
`AuditLog` tiene `OperationType operationType` (enum ✓) y `LocalDateTime operationDateTime` ✓. Sin embargo, usa `String userRole` y `String detailDataJson` en lugar de `Map<String, Object>` para datos flexibles. La bitácora existe pero le falta la estructura flexible esperada.

### 8. Reglas básicas de negocio — 4/5
`BankAccount` tiene métodos `validateState()` y `hasSingleHolder()` con lógica de negocio ✓. También hay servicios de dominio: `AuditLogService`, `CompanyClientService`. Buena implementación de reglas básicas.

### 9. Estructura del proyecto — 5/5
Arquitectura hexagonal completa: `domain/models`, `domain/ports`, `domain/services`, `controllers`, `dto`, `repository`, `exception`. Excelente separación por responsabilidades.

### 10. Repositorio — 2/5
- **Nombre:** `ConstruccionDeSoftware2WilmerVega` — correcto.
- **README:** Solo el nombre del repositorio.
- **Commits:** Descripciones en español (`refactor: Agregar ...`, `refactor: Actualizar ...`). No usa ADD/CHG pero usa convención `refactor:/feat:`.
- **Ramas:** Tiene `develop` ✓ (fusionada a main mediante PR). También hay documentación extensa (diagramas, docs de exposición).
- **Tag:** No hay tag de entrega.

---

## Fortalezas
- Modelo de dominio muy completo.
- Todos los enums esperados implementados.
- Uso correcto de `BigDecimal`.
- Arquitectura hexagonal bien implementada.
- Documentación adicional (diagramas, documentación de exposición) — valor añadido.
- Lógica de negocio en `BankAccount`.

## Oportunidades de mejora
- Crear una clase `Client` abstracta base para `NaturalClient` y `CompanyClient` (evitar la herencia `CompanyClient extends NaturalClient` que es semánticamente incorrecto).
- Agregar `Map<String, Object>` en `AuditLog` para datos flexibles.
- Referenciar `Client` (base) desde `User` en lugar de solo `NaturalClient`.
- Mejorar README con info de materia e integrantes.
- Agregar tag de entrega.
- Usar `java.time.LocalDate` en lugar de `java.sql.Date`.
