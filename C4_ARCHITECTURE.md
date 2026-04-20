# Arquitectura del Sistema: Core Banking (`wilmer-vega`)

Este documento resume la arquitectura con el modelo C4. Los diagramas están escritos en Mermaid para renderizarse en GitHub y en el editor.

## Nivel 1: Contexto

```mermaid
graph TD
    Usuario((Usuario del sistema))
    Sistema[Core Banking System\nwilmer-vega]

    Usuario -- "Usa la interfaz web o consume la API\nHTTPS / JWT" --> Sistema

    classDef person fill:#08427b,stroke:#052e56,color:#fff;
    classDef system fill:#1168bd,stroke:#0b4884,color:#fff;

    class Usuario person;
    class Sistema system;
```

El sistema es utilizado por administradores, analistas, supervisores, empleados y clientes. Todos interactúan mediante la UI visual o mediante la API REST protegida.

## Nivel 2: Contenedores

```mermaid
graph TD
    Usuario((Usuario))

    subgraph Sistema[Core Banking System]
        UI[Static Web UI\n/index.html]
        API[Spring Boot App\nJava 17 / Hexagonal]
        SQL[(H2 Database)]
        MONGO[(MongoDB Audit Store)]
    end

    Usuario -- "Navega y ejecuta servicios" --> UI
    Usuario -- "Consume endpoints REST" --> API
    API -- "Persistencia transaccional" --> SQL
    API -- "Auditoría de operaciones" --> MONGO

    classDef person fill:#08427b,stroke:#052e56,color:#fff;
    classDef container fill:#438dd5,stroke:#2e6295,color:#fff;
    classDef db fill:#438dd5,stroke:#2e6295,color:#fff;

    class Usuario person;
    class UI,API container;
    class SQL,MONGO db;
```

La interfaz estática vive junto a la aplicación y sirve como panel de demostración. H2 conserva el estado del negocio y MongoDB guarda la bitácora.

## Nivel 3: Componentes

```mermaid
graph TD
    subgraph API[Spring Boot API]
        Controllers[REST Controllers]
        UseCases[Application Use Cases]
        DomainServices[Domain Services]
        Ports[Domain Ports]
        SqlAdapters[SQL Persistence Adapters]
        MongoAdapters[Mongo Audit Adapters]
        Repositories[JPA / Mongo Repositories]
    end

    Controllers --> UseCases
    UseCases --> DomainServices
    DomainServices --> Ports
    SqlAdapters -. implementa .-> Ports
    MongoAdapters -. implementa .-> Ports
    SqlAdapters --> Repositories
    MongoAdapters --> Repositories

    classDef service fill:#85bbf0,stroke:#5d82a8,color:#000;
    classDef port fill:#dbdbdb,stroke:#868686,color:#000;
    classDef adapter fill:#438dd5,stroke:#2e6295,color:#fff;

    class UseCases,DomainServices service;
    class Ports port;
    class Controllers,SqlAdapters,MongoAdapters,Repositories adapter;
```

El dominio no depende de Spring ni de la base de datos. Los adaptadores implementan los puertos y mantienen la inversión de dependencias.

## Resumen técnico

- Frontend estático para demostración rápida.
- API REST con JWT y seguridad por roles.
- Persistencia operativa en H2.
- Bitácora técnica en MongoDB.
- Estructura hexagonal con separación clara entre dominio, aplicación e infraestructura.
