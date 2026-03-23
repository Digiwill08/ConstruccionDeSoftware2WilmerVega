# Evidencia de Entregas — Sistema Bancario (DDD)

## Entrega 1 (15 de marzo, 10%): Modelo del banco

Se implementó el modelo de dominio en `domain/models` (plural), con entidades y enums del negocio bancario.

Incluye, entre otras clases:
- `CompanyClient`, `NaturalClient`, `Person`
- `BankAccount`, `Loan`, `Transfer`, `BankingProduct`
- Enums de apoyo: `AccountStatus`, `LoanStatus`, `TransferStatus`, `Currency`

## Entrega 2 (5 de abril, 10%): Puertos y servicios

### Puertos configurados
- API Spring Boot: puerto por defecto `8080` (no se redefine `server.port`)
- MySQL: `localhost:3306` (`spring.datasource.url`)
- MongoDB: `localhost:27017` (`spring.data.mongodb.uri`)

### Servicios expuestos (API)
- Base ` /api/company-clients`
	- `GET /api/company-clients`
	- `GET /api/company-clients/{id}`
	- `POST /api/company-clients`
	- `DELETE /api/company-clients/{id}`
- Base ` /api/audit-logs`
	- `GET /api/audit-logs`
	- `GET /api/audit-logs/user/{userId}`
	- `GET /api/audit-logs/product/{productId}`
	- `POST /api/audit-logs`

### Seguridad aplicada a servicios
- Rutas `/api/**` requieren autenticación.
- Mecanismo: HTTP Basic.

## Resumen de cumplimiento

- Entrega 1: **Cumplida** (modelo DDD implementado en `models`).
- Entrega 2: **Cumplida** (puertos y servicios implementados y configurados).