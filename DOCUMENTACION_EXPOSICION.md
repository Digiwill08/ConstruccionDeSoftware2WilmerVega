# Documentación para Exposición — Sistema Bancario

## 1. Objetivo del proyecto
Este proyecto implementa una API bancaria con Spring Boot para gestionar:
- Clientes empresariales
- Auditoría de operaciones
- Modelo de dominio bancario (cuentas, préstamos, transferencias y usuarios)

---

## 2. Arquitectura en una vista rápida
El sistema está organizado por capas:

1. **Controller**: recibe solicitudes HTTP
2. **Service**: aplica reglas de negocio
3. **Repository**: guarda y consulta datos
4. **Domain Models**: representa entidades y estados del banco

Flujo general:

`Cliente HTTP -> Controller -> Service -> Repository -> Base de datos -> Respuesta JSON`

---

## 3. Tecnologías usadas
- Java 17
- Spring Boot
- Spring Web
- Spring Security
- Spring Data MongoDB
- Spring Data JPA (preparado)
- MySQL (configurado)
- MongoDB (en uso para endpoints actuales)
- Lombok

---

## 4. Seguridad
La seguridad está definida en `SecurityConfig`:
- Rutas `/api/**` requieren autenticación
- Se usa HTTP Basic
- CSRF está deshabilitado

Conclusión: sin credenciales válidas no se puede consumir la API principal.

---

## 5. Endpoints principales

## 5.1 Clientes de empresa
Base: `/api/company-clients`

- `GET /api/company-clients` → listar
- `GET /api/company-clients/{id}` → buscar por id
- `POST /api/company-clients` → crear
- `DELETE /api/company-clients/{id}` → eliminar

## 5.2 Auditoría
Base: `/api/audit-logs`

- `GET /api/audit-logs` → listar logs
- `GET /api/audit-logs/user/{userId}` → filtrar por usuario
- `GET /api/audit-logs/product/{productId}` → filtrar por producto
- `POST /api/audit-logs` → registrar log

---

## 6. Explicación paso a paso (caso real)
Ejemplo: consultar logs de un usuario

1. Cliente llama `GET /api/audit-logs/user/10`
2. Seguridad valida autenticación
3. `AuditLogController` recibe la petición
4. `AuditLogService` procesa la consulta
5. `AuditLogRepository` busca en MongoDB
6. Se devuelve lista de logs en JSON

---

## 7. Modelo de dominio (resumen)
El dominio incluye:
- Personas, clientes y usuarios del sistema
- Productos bancarios
- Cuentas bancarias
- Préstamos
- Transferencias
- Tipos/estados mediante enums

Regla importante implementada en código:
- Una cuenta bancaria debe tener un único titular válido (natural o empresa, no ambos).

---

## 8. Bases de datos
Configuración actual:
- MySQL configurado en propiedades
- MongoDB configurado y usado por los repositorios activos del API mostrada

Mensaje para exposición:
- El proyecto está preparado para arquitectura híbrida SQL + NoSQL.

---

## 9. Fortalezas actuales
- Buena separación por capas
- Modelo de dominio amplio
- Seguridad básica activa
- Endpoints funcionales para clientes empresa y auditoría

---

## 10. Mejoras sugeridas (siguiente fase)
1. Reducir duplicidad en métodos del servicio de clientes
2. Definir claramente qué módulos van en MySQL y cuáles en MongoDB
3. Estandarizar manejo de errores con un controlador global de excepciones
4. Usar DTO + mapper en todos los endpoints

---

## 11. Guion corto para exponer (2–3 minutos)
1. Presentar objetivo del sistema bancario
2. Mostrar arquitectura por capas
3. Explicar seguridad en `/api/**`
4. Demostrar 2 módulos activos: clientes empresa y auditoría
5. Cerrar con mejoras planeadas y siguiente evolución del proyecto

---

## 12. Conclusión
El sistema ya tiene base sólida para crecer: estructura limpia, seguridad configurada, endpoints funcionales y dominio bancario bien modelado para extender nuevas funcionalidades.