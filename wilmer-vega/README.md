# Wilmer Vega Bank

Aplicación bancaria desarrollada con Spring Boot y Maven.

## Requisitos

- Java 17
- Maven Wrapper (`mvnw.cmd` en Windows)
- MySQL ejecutándose localmente

## Ejecución

En Windows:

```powershell
./mvnw.cmd spring-boot:run
```

La aplicación expone el backend en `http://localhost:8083`.

## Documentación útil

- Arquitectura DDD y Clean Architecture: [docs/arquitectura-ddd.md](docs/arquitectura-ddd.md)
- Endpoints para Postman: [URLS_POSTMAN.md](URLS_POSTMAN.md)
- Modelo de dominio: [src/main/java/gestiondeunbanco/wilmervega/domain/models/README.md](src/main/java/gestiondeunbanco/wilmervega/domain/models/README.md)

## Notas

- El proyecto usa autenticación JWT.
- El login y el registro están disponibles en `/auth/*` y `/api/auth/*`.
- Los recursos administrativos y operativos están organizados por rol en controladores separados.
