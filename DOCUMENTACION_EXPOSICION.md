# Documentación para Exposición — Sistema Bancario

## 1. Objetivo del proyecto

Este proyecto implementa una plataforma bancaria REST con autenticación JWT, control por roles y una interfaz visual para ejecutar servicios funcionales. El sistema cubre:

- Clientes naturales y empresas.
- Cuentas bancarias y transferencias.
- Préstamos con ciclo de aprobación, rechazo y desembolso.
- Bitácora de auditoría consultable por rol.
- Registro público limitado para tipos de usuario autorizados.

## 2. Arquitectura en una vista rápida

El sistema sigue arquitectura hexagonal. La aplicación está separada en cuatro capas:

1. Capa Web: controladores REST y la interfaz visual en la ruta raíz.
2. Capa de Aplicación: casos de uso que coordinan operaciones por rol.
3. Capa de Dominio: modelos, puertos y reglas puras de negocio, sin dependencia de Spring.
4. Capa de Infraestructura: adaptadores de persistencia para H2 y MongoDB.

Flujo general:

`Cliente HTTP -> Controller -> UseCase -> Dominio -> Puerto -> Adaptador -> Repositorio -> Persistencia`

## 3. Tecnologías usadas

- Java 17
- Spring Boot 4.0.3
- Spring Web
- Spring Security + JWT
- Spring Data JPA con H2
- Spring Data MongoDB
- Lombok
- Maven

## 4. Módulos y endpoints principales

Autenticación:

- `POST /auth/login`
- `POST /auth/register`

Clientes y cuentas:

- `GET /api/customers/natural`
- `GET /api/customers/company`
- `GET /api/accounts`
- `POST /api/accounts`

Transferencias:

- `GET /api/transfers`
- `POST /api/transfers`
- `POST /api/transfers/approve`
- `POST /api/transfers/reject`
- `GET /api/transfers/pending-approval`

Préstamos:

- `GET /api/loans`
- `POST /api/loans`
- `POST /api/loans/approve`
- `POST /api/loans/reject`
- `POST /api/loans/disburse`

Administración y auditoría:

- `GET /api/admin/users`
- `POST /api/admin/users`
- `GET /api/admin/audit-logs`
- `GET /api/audit-logs`
- `GET /api/analyst/audit-logs`
- `GET /api/supervisor/transfers/pending`

## 5. Explicación paso a paso

Ejemplo: crear una transferencia (`POST /api/transfers`).

1. El cliente envía la solicitud al controlador `TransferController`.
2. El controlador valida el contrato y delega al caso de uso correspondiente.
3. La capa de aplicación decide la ruta de negocio según el rol y el estado de la transferencia.
4. El dominio aplica reglas: cuenta válida, monto permitido, estado correcto y lógica de aprobación si aplica.
5. El puerto de persistencia abstrae el almacenamiento y el adaptador escribe en el repositorio técnico.
6. La bitácora se registra en MongoDB para trazabilidad posterior.

## 6. Modelo de dominio

El modelo incluye:

- `NaturalClient` y `CompanyClient`.
- `BankAccount`, `Loan`, `Transfer` y `AuditLog`.
- `User`, `SystemUser`, `Person` y `UserManager`.
- Enums para estados, roles, tipos de cuenta, tipos de préstamo, divisas y categorías.

La regla central es que el dominio no depende de framework. Las implementaciones de persistencia viven fuera del núcleo de negocio.

## 7. Fortalezas logradas

- Separación clara de responsabilidades.
- Autenticación y autorización por roles.
- Flujo completo de préstamos y transferencias.
- Registro público limitado y controlado.
- Interfaz visual para demostrar servicios sin usar Postman.

## 8. Guion corto para exponer

1. Presentar el objetivo: un sistema bancario con seguridad JWT y arquitectura hexagonal.
2. Mostrar la separación entre web, aplicación, dominio e infraestructura.
3. Explicar que H2 maneja la transacción principal y MongoDB conserva la auditoría.
4. Demostrar un caso real: login, consulta de servicios y ejecución de una transferencia o préstamo.
5. Cerrar mostrando el panel visual, los roles y la documentación de soporte.