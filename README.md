# Construcción de Software 2 - Wilmer Vega

## Información del Proyecto

- **Materia:** Construcción de Software 2
- **Estudiantes Integrantes:** Wilmer Vega (Digiwill08)
- **Profesor:** Andrés Felipe Sánchez Aguiar
- **Proyecto:** Sistema de Gestión Bancaria (API RESTful)
- **Arquitectura:** Arquitectura Hexagonal Estricta (Puertos y Adaptadores)

## Descripción

Este repositorio contiene la implementación backend de un sistema bancario altamente escalable. El proyecto simula las operaciones cotidianas de un banco, incluyendo:
- Gestión de clientes (Personas Naturales y Empresas).
- Cuentas Bancarias y Préstamos.
- Transferencias de fondos con validaciones de negocio.
- Sistema puro de roles y usuarios.
- Auditoría integral de operaciones.

## Estructura de Arquitectura Hexagonal

El proyecto ha sido refactorizado y diseñado siguiendo los principios de la **Arquitectura Hexagonal (Puertos y Adaptadores)** exigidos en clase, dividiéndose en 4 capas estrictas para cada entidad del sistema:

1. **Capa Web (`controller`):** Exposición de la API HTTP (REST). Recibe peticiones y se comunica exclusivamente con la capa de aplicación.
2. **Capa de Aplicación (`application/usecases`):** Clases orquestadoras (`@Service`) que coordinan las transacciones y conectan los controladores con el dominio.
3. **Capa de Dominio (`domain`):**
   - **`services`:** Contiene la lógica pura de negocio (Java puro sin dependencias de frameworks).
   - **`ports`:** Interfaces de salida que establecen los contratos de infraestructura.
   - **`models`:** Entidades ricas y enumeraciones que definen las reglas base del banco.
4. **Capa de Infraestructura (`infrastructure/adapters`):** Implementaciones reales conectadas a las bases de datos (`@Component` y `JpaRepository`), aislando a Spring Data de la lógica de negocio.

## Tecnologías Utilizadas
- **Java 17**
- **Spring Boot 3** (Web, Data JPA)
- **MySQL / Base de Datos Relacional**
- **Maven** (Gestión de dependencias)
- **Lombok** (Reducción de boilerplate)

## Instalación y Ejecución

Para correr el proyecto localmente, asegúrese de tener Java 17 instalado:

```bash
# Otorgar permisos al wrapper (Linux/Mac)
chmod +x mvnw

# Compilar el proyecto
./mvnw clean compile

# Ejecutar el servidor
./mvnw spring-boot:run
```
La aplicación se levantará exponiendo sus endpoints protegidos bajo la ruta `/api/**`.