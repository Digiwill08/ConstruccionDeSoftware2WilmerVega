# Arquitectura del Sistema: Core Banking (`wilmer-vega`)

Este documento describe la arquitectura de software del sistema bancario utilizando el estándar **Modelo C4**. 
Los diagramas están generados utilizando Mermaid, lo que permite su renderizado nativo en plataformas como GitHub.

## Nivel 1: Diagrama de Contexto (Context Diagram)

El diagrama de contexto muestra el sistema en su totalidad y cómo interactúa con los usuarios y entidades externas.

```mermaid
graph TD
    %% Nodos
    User((Usuario Bancario\nAdministrador/Cliente))
    System[Core Banking System\nwilmer-vega]
    
    %% Relaciones
    User -- "Interactúa y administra cuentas\n[HTTPS / Web UI]" --> System
    
    %% Estilos (C4 Colors)
    classDef person fill:#08427b,stroke:#052e56,color:#fff;
    classDef system fill:#1168bd,stroke:#0b4884,color:#fff;
    
    class User person;
    class System system;
```

**Descripción:**
- **Usuario Bancario:** Representa a los actores del sistema (Administradores, Empleados y Clientes Naturales o Jurídicos) que interactúan con la plataforma.
- **Core Banking System:** El sistema central que gestiona préstamos, transferencias, cuentas de banco, registros de auditoría y la base de clientes.

---

## Nivel 2: Diagrama de Contenedores (Container Diagram)

Al hacer "zoom" en el Core Banking System, observamos los contenedores técnicos que ejecutan la solución.

```mermaid
graph TD
    %% Nodos
    User((Usuario Bancario))
    
    subgraph Core Banking System [Sistema Central Bancario]
        API[Spring Boot Application\nJava 17 / Hexagonal Arc.]
        DB[(Banco de Datos\nRelational Database SQL)]
    end
    
    %% Relaciones
    User -- "Consume endpoints REST\n[JSON/HTTPS]" --> API
    API -- "Lee y escribe datos transaccionales\n[JDBC/JPA]" --> DB
    
    %% Estilos
    classDef person fill:#08427b,stroke:#052e56,color:#fff;
    classDef container fill:#438dd5,stroke:#2e6295,color:#fff;
    classDef db fill:#438dd5,stroke:#2e6295,color:#fff;
    
    class User person;
    class API container;
    class DB db;
```

**Descripción:**
- **Spring Boot Application:** Es el corazón del sistema, expone una API RESTFul, contiene toda la lógica de negocio y obedece al diseño Hexagonal.
- **Banco de Datos (SQL):** Repositorio de la información persistente, almacena el historial inmutable de transferencias (AuditLog) y el estado financiero actual.

---

## Nivel 3: Diagrama de Componentes (Component Diagram)

Acercándonos hacia dentro del contenedor del Backend o API, visualizamos cómo la estructura se alinea a los principios de la **Arquitectura Hexagonal (Ports and Adapters)**.

```mermaid
graph TD
    subgraph Spring Boot API [Contenedor Spring Boot API]
        
        subgraph Capa Infraestructura [Application Layer - Adapters]
            WebControllers[Web REST Controllers\nAdaptadores de Entrada HTTP]
            PersistenceAdapters[SQL Persistence Adapters\nAdaptadores de Salida]
            JPARepositories[JPA Repositories\nSpring Data Interfaces]
        end
        
        subgraph Capa Core [Domain Layer - Nucleo Hexagonal]
            DomainServices[Domain Services\nOperaciones: Create, Find, Update, Delete]
            DomainPorts[Domain Ports\nInterfaces protectoras del Dominio]
            DomainModels[Domain Models\nEntidades ricas: Person, BankAccount, etc.]
        end
    end
    
    %% Relaciones Hexagonales
    WebControllers -- "Invoca Casos de Uso" --> DomainServices
    DomainServices -- "Implementa lógicas sobre" --> DomainModels
    DomainServices -- "Depende de abstracciones" --> DomainPorts
    
    %% Inversion de Dependencias
    PersistenceAdapters -. "Implementa (Inversión de Dependencia)" .-> DomainPorts
    PersistenceAdapters -- "Usa operaciones CRUD" --> JPARepositories
    PersistenceAdapters -- "Transforma Data (Mappers)" --> DomainModels
    
    %% Estilos
    classDef service fill:#85bbf0,stroke:#5d82a8,color:#000;
    classDef port fill:#dbdbdb,stroke:#868686,color:#000;
    classDef model fill:#ffcc00,stroke:#b28e00,color:#000;
    classDef adapter fill:#438dd5,stroke:#2e6295,color:#fff;
    
    class DomainServices service;
    class DomainPorts port;
    class DomainModels model;
    class WebControllers,PersistenceAdapters,JPARepositories adapter;
```

**Descripción de la Inversión de Dependencias (DIP):**
Como exige la Arquitectura Hexagonal, el Dominio (servicios, puertos y modelos) **no tiene dependencias externas**. Es la Capa de Infraestructura (`PersistenceAdapters`) la que implementa los `DomainPorts`, garantizando que la lógica del banco pueda existir y probarse con independencia de la base de datos subyacente.
