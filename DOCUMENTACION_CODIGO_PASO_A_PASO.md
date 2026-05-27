# Documentación del Código — Paso a Paso

## 1) ¿Qué es este proyecto?

Es una aplicación bancaria construida con Spring Boot y Java 17. La solución resuelve:

- Autenticación JWT.
- Registro de usuarios con restricciones por rol.
- Gestión de clientes naturales y empresas.
- Gestión de cuentas, transferencias y préstamos.
- Bitácora de auditoría en MongoDB.

La aplicación usa arquitectura hexagonal:

- Capa Web: controladores REST y panel visual.
- Capa de Aplicación: casos de uso.
- Capa de Dominio: modelos, servicios y puertos.
- Capa de Infraestructura: adaptadores de persistencia.

## 2) Estructura general del proyecto

Dentro de `wilmer-vega/src/main/java/gestiondeunbanco/wilmervega`:

- `WilmerVegaApplication.java`: arranque de la aplicación.
- `application/adapters/api/`: controladores REST.
- `application/adapters/persistence/`: adaptadores SQL y Mongo.
- `application/usecases/`: orquestación de reglas por rol.
- `config/`: seguridad, beans y conectividad.
- `domain/models/`: entidades y enums de negocio.
- `domain/ports/`: contratos de salida.
- `domain/services/`: reglas puras del negocio.

## 3) Flujo de una petición REST

Ejemplo: `POST /api/transfers`.

1. El cliente llama al controlador de transferencias.
2. El controlador valida el contrato y delega al caso de uso.
3. El caso de uso aplica la lógica por rol y decide si la operación se ejecuta o queda pendiente.
4. El dominio verifica reglas: monto, estado, cuentas y aprobación si corresponde.
5. El puerto define qué necesita persistirse sin acoplarse a la base de datos.
6. El adaptador implementa el puerto y guarda la información.
7. La bitácora registra la operación en MongoDB.

## 4) Endpoints disponibles

Autenticación:

- `POST /auth/login`
- `POST /auth/register`

Administración:

- `GET /api/admin/users`
- `GET /api/admin/users/{id}`
- `GET /api/admin/users/username/{username}`
- `POST /api/admin/users`
- `DELETE /api/admin/users/{id}`
- `GET /api/admin/audit-logs`

Analista:

- `GET /api/analyst/loans`
- `POST /api/analyst/loans/{id}/approve`
- `POST /api/analyst/loans/{id}/reject`
- `POST /api/analyst/loans/{id}/disburse`
- `GET /api/analyst/audit-logs`

Supervisor:

- `GET /api/supervisor/transfers/pending`
- `POST /api/supervisor/transfers/{id}/approve`
- `POST /api/supervisor/transfers/{id}/reject`

Cliente:

- `GET /api/client/bank-accounts/{accountNumber}`
- `GET /api/client/transfers`
- `POST /api/client/transfers`

Empleado:

- `GET /api/employee/bank-accounts`
- `POST /api/employee/bank-accounts`
- `GET /api/employee/natural-clients`
- `POST /api/employee/natural-clients`
- `GET /api/employee/company-clients`
- `POST /api/employee/company-clients`
- `GET /api/employee/loans`
- `POST /api/employee/loans`

## 5) Diseño de clases

### Jerarquía de dominio

- `Person` -> `Client` -> `NaturalClient` / `CompanyClient`
- `Person` -> `UserManager` -> `User` / `SystemUser`

### Portabilidad del dominio

El dominio no llama directamente a repositorios de Spring. Todo pasa por interfaces como `AuditLogPort`, `TransferPort`, `LoanPort` y `UserPort`.

## 6) Configuración de seguridad

La seguridad se centraliza en `SecurityConfig.java`.

1. CSRF está deshabilitado para la API.
2. Se usa autenticación JWT.
3. Las rutas públicas se limitan a login, registro y la interfaz visual.
4. Los demás endpoints se protegen por rol.

## 7) Guía de lectura rápida

1. Lee un modelo, por ejemplo `BankAccount`.
2. Revisa su puerto, por ejemplo `BankAccountPort`.
3. Observa el servicio de dominio que implementa las reglas.
4. Sigue el caso de uso en `application/usecases`.
5. Mira el controlador REST que expone la ruta.
6. Finaliza en el adaptador de persistencia que conecta con H2 o Mongo.
