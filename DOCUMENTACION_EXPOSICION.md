# Documentación para Exposición — Sistema Bancario

## 1. Objetivo del proyecto
Este proyecto implementa una API bancaria completa y escalable con Spring Boot para gestionar:
- Clientes (Naturales y Empresariales)
- Auditoría de operaciones
- Modelo de dominio bancario rico (Cuentas, Préstamos, Transferencias y Usuarios)

---

## 2. Arquitectura en una vista rápida
El sistema está organizado bajo la estricta **Arquitectura Hexagonal (Puertos y Adaptadores)**, dividida en 4 capas puras:

1. **Capa Web (Controller)**: Recibe solicitudes HTTP y delega al Caso de Uso.
2. **Capa de Aplicación (UseCase)**: Orquesta las transacciones y conecta la entrada con el dominio.
3. **Capa de Dominio (Domain Services & Ports)**: Java PURO sin dependencias de Spring. Aquí viven las validaciones financieras reales y se declaran los "Puertos" (interfaces de cómo queremos guardar datos).
4. **Capa de Infraestructura (Adapters & Repositories)**: Conecta la aplicación con MySQL. Aquí viven los adaptadores que satisfacen los puertos del dominio usando Spring Data JPA.

Flujo general:
`Cliente HTTP -> Controller -> UseCase -> Domain Service (usa Ports) -> Port -> Persistence Adapter -> JPA Repository -> BD`

---

## 3. Tecnologías usadas
- Java 17
- Spring Boot 3
- Spring Web
- Spring Data JPA
- MySQL
- Lombok

---

## 4. Endpoints principales (Módulos activos)
El sistema expone de forma RESTFUL todos los dominios integrados a la nueva arquitectura. Cada módulo tiene su Controlador web con CRUD y listados:

- `/api/company-clients` → Empresas
- `/api/natural-clients` → Clientes naturales
- `/api/bank-accounts` → Cuentas bancarias
- `/api/loans` → Préstamos
- `/api/transfers` → Transferencias
- `/api/users` → Usuarios
- `/api/audit-logs` → Bitácora

---

## 5. Explicación paso a paso (caso real)
Ejemplo: Crear una transferencia bancaria (`POST /api/transfers`)

1. **Web:** `TransferController` recibe el JSON y llama a `TransferUseCase`.
2. **Aplicación:** `TransferUseCase` (con `@Service`) inicia el proceso y se lo pasa a `TransferDomainService`.
3. **Dominio:** `TransferDomainService` aplica reglas 100% Java (ej. verificar que el monto no sea 0 o negativo). Si todo está en orden, usa el `TransferPort` para pedir que se guarde.
4. **Infraestructura:** Spring inyecta el `TransferPersistenceAdapter` (que implementa `TransferPort`), el cual usa finalmente `TransferRepository` de Spring Data JPA para insertar el registro en MySQL.
5. El flujo retorna hacia arriba devolviendo un código `200 OK`.

---

## 6. Modelo de dominio (resumen)
El dominio incluye:
- Jerarquía de clientes y usuarios.
- Productos bancarios interactuando.
- Cuentas, Préstamos y Transferencias.
- Reglas controladas por Enums de estado limpios.

Regla importante implementada en código:
- Cero acoplamiento: La lógica de `domain/services` NO importa librerías de Spring Boot. Toda persistencia se abstrae en puertos.

---

## 7. Fortalezas logradas en la refactorización
- **Separación absoluta de responsabilidades** (Arquitectura Hexagonal al 100%).
- Eliminación del código legado (Capas *Service* acopladas a la base de datos).
- El modelo de dominio principal es totalmente testeable sin necesidad de levantar bases de datos.

---

## 8. Guion corto para exponer (2–3 minutos)
1. "Este es el Sistema de Gestión Bancaria que he mejorado aplicando el patrón de Arquitectura Hexagonal que vimos en clase, basado en el ejemplo de la clínica."
2. "Hemos pasado de una arquitectura simple en capas a una purista dividida en 4: Controladores, Casos de Uso, Dominio e Infraestructura."
3. "Demostrar cómo en `domain/services` nuestro código ya no depende de `@Service` ni de `Spring`, aislando el corazón del negocio."
4. "Explicar el flujo completo usando, por ejemplo, el proceso de Préstamos o Transferencias. Entrar por el Controller -> UseCase -> Service Puro -> Adapter."
5. "Concluir mostrando que el sistema cuenta con todas las entidades bancarias clave sin deudas técnicas, dejándolo listo para producción."