# Microservicio para realizar operaciones aritméticas

---

## Aspectos técnicos

Nota: Para ejecutar el proyecto, se requiere la instalación del JDK 17.

El servicio está construido con las siguientes características:

- Java 17
- Spring WebFlux
- Spring Boot 3.4.4
- Flyway (para realizar actualizaciones de base de datos)
- Supabase (PostgreSQL 15)

## Crear base de datos Postgresql

Se requiere la creación de una base de datos PostgreSQL 15+, los valores como el HOST, PORT USER y PASSWORD, serán requeridos en en paso siguiente.

## Crear archivo .env para configuraciones

En el siguiente archivo se requiren los valores de la base de datos creada previamente.

```properties
 SERVER_PORT=8080
 SPRING_APPLICATION_NAME="ms-arithmetic-calculator"
 SPRING_PROFILES_INCLUDE=
 SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_SECRET_KEY="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"
 R2DBC_URL="r2dbc:postgresql://aws-0-us-east-2.pooler.supabase.com:5432/postgres"
 R2DBC_USERNAME="postgres.rdmrosepmquohufhiams"
 R2DBC_PASSWORD="jdepaz2025"
 R2DBC_HOST="aws-0-us-east-2.pooler.supabase.com"
 R2DBC_PORT=5432
 R2DBC_DATABASE="postgres"
 FLYWAY_URL="jdbc:postgresql://aws-0-us-east-2.pooler.supabase.com:5432/postgres"
 FLYWAY_USER="postgres.rdmrosepmquohufhiams"
 FLYWAY_PASSWORD="jdepaz2025"
 FLYWAY_HOST="aws-0-us-east-2.pooler.supabase.com"
 FLYWAY_PORT=5432
 FLYWAY_DATABASE="postgres"
 JWT_JSON_EXP_ROLES="/roles"
 CORS_ALLOWED_ORIGINS="http://localhost:4200,http://localhost:8080"
```

En el IDE de preferencia, se debe agregar o vincular el archivo `.env` para que pueda cargar las variables de ambiente.

---
## Endpoints

El servicio expone los siguientes endpoints:

### Registro de usuarios

```
 POST http://localhost:8080/api/auth/register
```
Body
```
 {
    "username": "jdepaz1",
    "password": "jdepaz1",
    "email": "josedepaz@gmail.com"
}
```

cURL

```bash
  curl --location 'http://localhost:8080/api/auth/register' \
  --header 'Content-Type: application/json' \
  --data-raw '{
  "username": "jdepaz2",
  "password": "jdepaz2",
  "email": "josedepaz2@gmail.com"
  }'
```

### Autenticación de usuarios

```
 POST http://localhost:8080/api/auth/login
```
Body
```
{
    "username": "jdepaz",
    "password": "jdepaz"
}
```

cURL

```bash
  curl --location 'http://localhost:8080/api/auth/login' \
  --header 'Content-Type: application/json' \
  --data '{
      "username": "jdepaz2",
      "password": "jdepaz2"
  }'
```
Si el usuario se autentica correctamente, la respuesta contiene el TOKEN JWT para uso posterior, el cual tiene una vigencia de 1 hora:

```
 {
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoiMTdhOGJiMmEtNzczZi00ODIwLTlkNGEtMGQ1NWRlYTEyM2ZmIiwiaWF0IjoxNzQ1MDA0NDQ4LCJleHAiOjE3NDUwMDgwNDh9.8es7l23QFh9V-fVe4k9cG414Vvn3FgELD6DhgdF8ITmdNikIzrtV6QqGRw45hl6QCHFtwU4XrGKJrZKwZMjC3Q"
  }
```

### Realizar cálculos

```
 POST http://localhost:8080/api/calculate
```
Body
```
{
    "operation": "DIVISION",
    "operandA": 10.5,
    "operandB": 0
}
```

cURL

  ```bash
   curl --location 'http://localhost:8080/api/calculate' \
   --header 'Content-Type: application/json' \
   --header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoiMmI5NTE1NmEtMTgzMi00ZTRlLTkyZGQtMDZlZTA5NTA2NzFhIiwiaWF0IjoxNzQ1MDI2MDgyLCJleHAiOjE3NDUwMjk2ODJ9.CR1y2ZyLbgmMvkouhkoG0ZUeRcnHuvdRI9pRjKY3Widb8kFFIeQB3jp60uKF6IaLOM0oEa7GZfxkT5XGvar9wg' \
   --data '{
    "operation": "ADDITION",
    "operandA": 10.5,
    "operandB": 5.0
    }'
  ```


Respuesta:

```
{
  "id": "c0bad022-3093-4f3f-86b3-ae067a84f062",
  "operation": "DIVISION",
  "operandA": 10.5,
  "operandB": 5.0,
  "result": 2.1,
  "timestamp": "2025-04-18T18:00:42.761847157",
  "userId": "17a8bb2a-773f-4820-9d4a-0d55dea123ff",
  "error": null
 }
```

**Nota**: En la carpeta ![postman](/postman/CALCULATOR.postman_collection.json) se encuentra el archivo *CALCULATOR.postman_collection.json* con la colección de REQUEST para realizar las pruebas.

---
# Flujo funcional


1. Realizar el registro de un usuario  (**_Registro de usuarios_**)
2. Autenticarse (login) con el usuario y password creado anteriormente (**_Autenticación de usuarios_**) 
   - La autenticación devuelve un token, el cual se utilizará como "Bearer" 
3. Realizar el cálculo (**_Realizar cálculos_**)

---
# Arquitectura

## Antes de Iniciar

Empezaremos por explicar los diferentes componentes del proyectos y partiremos de los componentes externos, continuando con los componentes core de negocio (dominio) y por último el inicio y configuración de la aplicación.

Lee el artículo [Clean Architecture — Aislando los detalles](https://medium.com/bancolombia-tech/clean-architecture-aislando-los-detalles-4f9530f35d7a)

# Arquitectura

![Clean Architecture](https://miro.medium.com/max/1400/1*ZdlHz8B0-qu9Y-QO3AXR_w.png)

## Domain

Es el módulo más interno de la arquitectura, pertenece a la capa del dominio y encapsula la lógica y reglas del negocio mediante modelos y entidades del dominio.

## Usecases

Este módulo gradle perteneciente a la capa del dominio, implementa los casos de uso del sistema, define lógica de aplicación y reacciona a las invocaciones desde el módulo de entry points, orquestando los flujos hacia el módulo de entities.

## Infrastructure

### Helpers

En el apartado de helpers tendremos utilidades generales para los Driven Adapters y Entry Points.

Estas utilidades no están arraigadas a objetos concretos, se realiza el uso de generics para modelar comportamientos
genéricos de los diferentes objetos de persistencia que puedan existir, este tipo de implementaciones se realizan
basadas en el patrón de diseño [Unit of Work y Repository](https://medium.com/@krzychukosobudzki/repository-design-pattern-bc490b256006)

Estas clases no puede existir solas y debe heredarse su compartimiento en los **Driven Adapters**

### Driven Adapters

Los driven adapter representan implementaciones externas a nuestro sistema, como lo son conexiones a servicios rest,
soap, bases de datos, lectura de archivos planos, y en concreto cualquier origen y fuente de datos con la que debamos
interactuar.

### Entry Points

Los entry points representan los puntos de entrada de la aplicación o el inicio de los flujos de negocio.

## Application

Este módulo es el más externo de la arquitectura, es el encargado de ensamblar los distintos módulos, resolver las dependencias y crear los beans de los casos de use (UseCases) de forma automática, inyectando en éstos instancias concretas de las dependencias declaradas. Además inicia la aplicación (es el único módulo del proyecto donde encontraremos la función “public static void main(String[] args)”.

**Los beans de los casos de uso se disponibilizan automaticamente gracias a un '@ComponentScan' ubicado en esta capa.**
