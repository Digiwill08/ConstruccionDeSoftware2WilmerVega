# Construcción de Software 2 — Wilmer Vega

## Información del Proyecto

| Campo | Detalle |
|---|---|
| Materia | Construcción de Software 2 (220261) |
| Estudiante | Wilmer Vega (GitHub: Digiwill08) |
| Profesor | Andrés Felipe Sánchez Aguiar |
| Proyecto | Aplicación de Gestión Bancaria con arquitectura hexagonal |

## Resumen

Sistema bancario orientado a servicios REST con autenticación JWT, roles por perfil, panel web visual para ejecutar servicios y bitácora de auditoría en MongoDB. La persistencia principal de negocio usa H2 en memoria para facilitar la ejecución local y la demostración de la entrega.

## Arquitectura

El proyecto está organizado con arquitectura hexagonal:

- `domain/`: modelos, puertos y reglas del negocio.
- `application/usecases/`: orquestación por caso de uso y por rol.
- `application/adapters/api/`: controladores REST.
- `application/adapters/persistence/`: adaptadores de persistencia para SQL y Mongo.
- `config/`: seguridad, beans y verificación de conexión.

## Tecnologías

| Tecnología | Uso |
|---|---|
| Java 17 | Lenguaje principal |
| Spring Boot 4.0.3 | Framework base |
| Spring Web | API REST |
| Spring Security + JWT | Autenticación y autorización |
| Spring Data JPA | Persistencia relacional |
| H2 | Base de datos transaccional local |
| Spring Data MongoDB | Bitácora de auditoría |
| Lombok | Reducción de boilerplate |
| Maven | Construcción y dependencias |

## Roles

| Rol | Propósito |
|---|---|
| `ADMINISTRATOR` | Gestión total de usuarios y consulta de auditoría |
| `NATURAL_CLIENT` | Cliente persona natural |
| `COMPANY_CLIENT` | Cliente empresa |
| `TELLER_EMPLOYEE` | Operaciones de ventanilla |
| `COMMERCIAL_EMPLOYEE` | Gestión de clientes y préstamos |
| `COMPANY_EMPLOYEE` | Operaciones de empresa |
| `COMPANY_SUPERVISOR` | Aprobación o rechazo de transferencias |
| `INTERNAL_ANALYST` | Aprobación, rechazo y desembolso de préstamos |

## Funcionalidades clave

- Login JWT y registro público limitado a roles permitidos.
- Gestión de clientes naturales y empresas.
- Gestión de cuentas bancarias, transferencias y préstamos.
- Aprobación y rechazo de préstamos.
- Aprobación, rechazo y vencimiento de transferencias.
- Bitácora de operaciones consultable por rol.
- Panel visual en la ruta raíz para ejecutar servicios desde el navegador.

## Endpoints principales

Autenticación:

- `POST /auth/login`
- `POST /auth/register`

Clientes y cuentas:

- `GET /api/customers/natural`
- `GET /api/customers/company`
- `GET /api/accounts`
- `GET /api/accounts/{accountNumber}`
- `POST /api/accounts`

Transferencias:

- `GET /api/transfers`
- `GET /api/transfers/{transferId}`
- `GET /api/transfers/status/{status}`
- `POST /api/transfers`
- `POST /api/transfers/approve`
- `POST /api/transfers/reject`
- `GET /api/transfers/pending-approval`

Préstamos:

- `GET /api/loans`
- `GET /api/loans/{loanId}`
- `POST /api/loans`
- `POST /api/loans/approve`
- `POST /api/loans/reject`
- `POST /api/loans/disburse`

Administración y auditoría:

- `GET /api/admin/users`
- `POST /api/admin/users`
- `GET /api/audit-logs`
- `GET /api/analyst/audit-logs`
- `GET /api/supervisor/transfers/pending`

## Ejecución local

```bash
cd wilmer-vega
./mvnw spring-boot:run
```

La aplicación expone la interfaz visual en `/` y la API en `http://localhost:8083` por configuración por defecto. Para escenarios con Mongo local o Atlas, también existe `run-with-mongo.ps1`.

## Evidencia de entrega

- Colección Postman actualizada en `postman/`.
- Documentación de exposición y paso a paso alineada con la implementación real.
- Diagramas de arquitectura y modelo actualizados.