# ms-products

Backend de gestión de productos para una plataforma e-commerce multi-tenant, construido con Java 21 y Spring Boot 3.

## Tech Stack

- **Java 21** + Spring Boot 3.4.5
- **Spring Security** + JWT (access token + refresh token)
- **Spring Data JPA** + PostgreSQL
- **Redis** — cache con TTL configurable
- **MapStruct** — mapeo de DTOs
- **Lombok** — reducción de boilerplate
- **Springdoc OpenAPI** — documentación automática
- **Bucket4j** — rate limiting por IP
- **Docker** — contenedor multi-stage

## Requisitos previos

- JDK 21
- PostgreSQL 15+
- Redis 7+
- Maven 3.9+ (o usar `./mvnw`)

## Variables de entorno

| Variable | Descripción | Requerida |
|---|---|---|
| `JWT_SECRET` | Clave secreta JWT (mínimo 64 caracteres) | ✅ |
| `DB_URL` | URL de conexión PostgreSQL. Ej: `jdbc:postgresql://localhost:5432/msproducts` | ✅ |
| `DB_USER` | Usuario de la base de datos | ✅ |
| `DB_PASSWORD` | Contraseña de la base de datos | ✅ |
| `REDIS_HOST` | Host de Redis (prod/qa) | ✅ prod/qa |
| `REDIS_PASSWORD` | Contraseña de Redis | ❌ |
| `SPRING_PROFILES_ACTIVE` | Perfil activo: `dev`, `qa`, `prod` | ❌ (default: `dev`) |
| `SERVER_PORT` | Puerto del servidor | ❌ (default: `8080`) |
| `JWT_EXPIRATION` | Expiración del access token en ms | ❌ (default: `86400000` = 24h) |
| `CORS_ALLOWED_ORIGINS` | Orígenes permitidos separados por coma | ❌ (default: `http://localhost:3000,http://localhost:4200`) |

## Levantar en desarrollo

### 1. Clonar el repositorio

```bash
git clone https://github.com/sebcode/ms-products.git
cd ms-products
```

### 2. Configurar variables de entorno

```bash
export JWT_SECRET=mi_clave_super_secreta_de_al_menos_64_caracteres_para_firmar_tokens
export DB_URL=jdbc:postgresql://localhost:5432/msproducts
export DB_USER=postgres
export DB_PASSWORD=postgres
```

### 3. Ejecutar

```bash
./mvnw spring-boot:run
```

La app estará disponible en `http://localhost:8080`.

## Levantar con Docker

### Build de la imagen

```bash
docker build -t ms-products .
```

### Ejecutar el contenedor

```bash
docker run -p 8080:8080 \
  -e JWT_SECRET=tu_clave_secreta \
  -e DB_URL=jdbc:postgresql://host.docker.internal:5432/msproducts \
  -e DB_USER=postgres \
  -e DB_PASSWORD=postgres \
  ms-products
```

## Endpoints principales

### Autenticación (público)

| Método | Endpoint | Descripción |
|---|---|---|
| `POST` | `/api/v1/auth/login` | Login — devuelve `accessToken` + `refreshToken` |
| `POST` | `/api/v1/auth/refresh-token` | Renueva el access token con un refresh token válido |

### Catálogo de productos (público)

| Método | Endpoint | Descripción |
|---|---|---|
| `GET` | `/api/v1/variant-products` | Buscar productos con filtros y paginación |
| `GET` | `/api/v1/variant-products/{id}` | Detalle de un producto |
| `GET` | `/api/v1/variant-products/category/{id}` | Productos por categoría |
| `GET` | `/api/v1/variant-products/subcategory/{id}` | Productos por subcategoría |
| `GET` | `/api/v1/variant-products/best-selling` | Productos más vendidos |

### Administración (requiere rol ADMIN)

| Método | Endpoint | Descripción |
|---|---|---|
| `GET/POST/PUT/DELETE` | `/api/v1/products/**` | CRUD de productos |
| `GET/POST/PUT/DELETE` | `/api/v1/brands/**` | CRUD de marcas |
| `GET/POST/PUT/DELETE` | `/api/v1/categories/**` | CRUD de categorías |
| `GET/POST/PUT/DELETE` | `/api/v1/subcategories/**` | CRUD de subcategorías |
| `GET/POST/PUT/DELETE` | `/api/v1/attribute-types/**` | CRUD de tipos de atributo |
| `GET/POST/PUT/DELETE` | `/api/v1/attribute-values/**` | CRUD de valores de atributo |
| `GET/POST/PUT/DELETE` | `/api/v1/product-images/**` | CRUD de imágenes de producto |

### Health & Monitoreo

| Endpoint | Acceso | Descripción |
|---|---|---|
| `GET /actuator/health` | Público | Estado del servicio, BD y Redis |
| `GET /actuator/info` | ADMIN | Información de la aplicación |
| `GET /actuator/metrics` | ADMIN | Métricas de JVM y requests |

## Documentación API

Swagger UI disponible en `http://localhost:8080/swagger-ui.html` (requiere token ADMIN).

## Rate Limiting

| Endpoint | Límite |
|---|---|
| `/api/v1/auth/login` y `/api/v1/auth/refresh-token` | 10 requests/minuto por IP |
| `/api/v1/variant-products/**` | 100 requests/minuto por IP |

## Estructura del proyecto

```
src/main/java/com/sebcode/msproducts/
├── cache/              — Configuración de Redis
├── category/           — Módulo de categorías y subcategorías
├── common/             — Entidades base, DTOs y respuestas compartidas
├── company/            — Módulo de empresas (multi-tenant)
├── exception/          — Excepciones globales y handler
├── product/            — Módulo principal de productos, variantes, marcas
├── security/           — JWT, filtros, configuración de Spring Security
└── user/               — Módulo de usuarios
```

## Autor

**Sebastian Ortega** — [GitHub](https://github.com/sebcodestudio)