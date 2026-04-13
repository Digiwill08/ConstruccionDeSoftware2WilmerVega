# Construcción de Software 2 — Wilmer Vega

## Información del Proyecto

| Campo | Detalle |
|---|---|
| **Materia** | Construcción de Software 2 (220261) |
| **Estudiante** | Wilmer Vega (GitHub: Digiwill08) |
| **Profesor** | Andrés Felipe Sánchez Aguiar |
| **Proyecto** | Aplicación de Gestión Bancaria — Core Transaccional |

---

## Descripción

Sistema de información para la gestión de clientes, cuentas bancarias, préstamos y transferencias de una entidad bancaria. Implementa flujos de aprobación reales, reglas de negocio bancarias y auditoría completa de operaciones en una bitácora NoSQL.

---

## Arquitectura Hexagonal (Ports & Adapters)

```
domain/
  ├── models/      ← Entidades del negocio (POJOs puros, sin dependencias de framework)
  ├── ports/       ← Interfaces de entrada/salida (contratos del dominio)
  ├── services/    ← Lógica de negocio (CRUD + servicios de negocio: Approve/Reject/Disburse)
  └── exceptions/  ← Excepciones del dominio

application/
  ├── usecases/    ← Orquestación por rol (Admin, Employee, Client, Analyst, CompanySupervisor)
  └── adapters/
        ├── api/              ← Controladores REST
        ├── persistence/sql/  ← Adaptadores JPA/MySQL
        └── persistence/mongo/← Adaptadores MongoDB (Bitácora NoSQL)

config/  ← Spring Security con acceso por roles
```

---

## Tecnologías

| Tecnología | Uso |
|---|---|
| **Java 17** | Lenguaje de programación |
| **Spring Boot 4** | Framework principal |
| **Spring Data JPA + MySQL** | Datos relacionales (clientes, cuentas, préstamos, transferencias) |
| **Spring Data MongoDB** | Bitácora de operaciones NoSQL (documentos flexibles) |
| **Spring Security** | Autenticación y control de acceso por roles |
| **Lombok** | Reducción de boilerplate |
| **Maven** | Gestión de dependencias |

---

## Roles del Sistema

| Rol | Descripción |
|---|---|
| `NATURAL_CLIENT` | Cliente persona natural — consulta y opera sus propios productos |
| `COMPANY_CLIENT` | Cliente empresa — administra productos de la empresa |
| `TELLER_EMPLOYEE` | Cajero — depósitos, retiros, apertura de cuentas |
| `COMMERCIAL_EMPLOYEE` | Asesor comercial — gestión de solicitudes de productos |
| `COMPANY_EMPLOYEE` | Operativo de empresa — crea transferencias empresariales |
| `COMPANY_SUPERVISOR` | Supervisor de empresa — aprueba/rechaza transferencias de alto monto |
| `INTERNAL_ANALYST` | Analista interno — aprueba/rechaza/desembolsa préstamos, acceso a bitácora |

---

## Flujos de Negocio Implementados

### Flujo de Préstamos
```
Solicitud (UNDER_REVIEW) → Analista aprueba → (APPROVED) → Desembolso → (DISBURSED)
                         → Analista rechaza → (REJECTED)
```

### Flujo de Transferencias
```
Monto ≤ $10,000,000 COP → Ejecuta directo           → Estado: EXECUTED
Monto > $10,000,000 COP → Espera aprobación          → Estado: AWAITING_APPROVAL
                           Supervisor aprueba          → Estado: EXECUTED
                           Supervisor rechaza          → Estado: REJECTED
                           Sin respuesta en 60 minutos → Estado: EXPIRED (automático)
```

---

## Instalación y Ejecución

```bash
# Configurar en application.properties:
# - URL, usuario y contraseña de MySQL
# - URL de MongoDB (local o Atlas)

cd wilmer-vega
./mvnw spring-boot:run
```

API disponible en: `http://localhost:8080`

Endpoints principales:
- `/api/analyst/loans/{id}/approve` — Aprobar préstamo
- `/api/analyst/loans/{id}/disburse` — Desembolsar préstamo
- `/api/supervisor/transfers/pending` — Ver transferencias pendientes
- `/api/supervisor/transfers/{id}/approve` — Aprobar transferencia
- `/api/analyst/audit-logs` — Consultar bitácora completa