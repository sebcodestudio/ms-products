# 🛒 MS-Products - Multi-Tenant Ecommerce Backend

Spring Boot backend para plataforma de ecommerce multi-tenant (SaaS).

## 🚀 Features

- 🔐 Autenticación JWT
- 👥 Multi-tenant (múltiples tiendas en un solo sistema)
- 🏢 Gestión de compañías/tiendas
- 📦 Catálogo de productos con variantes
- 🛍️ Carrito de compras
- 📊 Sistema de roles (Admin, Owner, Manager, Seller)
- 🔍 Búsqueda y filtros avanzados
- 📝 Auditoría automática

## 🛠️ Tech Stack

- Java 21
- Spring Boot 3.x
- Spring Security + JWT
- Spring Data JPA
- MySQL 8.0
- Lombok
- MapStruct
- Maven

## 📋 Requisitos

- JDK 17+
- MySQL 8.0+
- Maven 3.8+

## ⚙️ Instalación

1. Clonar repositorio:
```bash
git clone https://github.com/SEBCODE/ms-products.git
cd ms-products
```

2. Configurar base de datos en `application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ecommerce_db
    username: root
    password: tu_password
```

3. Ejecutar:
```bash
./mvnw spring-boot:run
```

## 📚 API Documentation

Swagger UI: `http://localhost:8080/swagger-ui.html`

## 👨‍💻 Autor

**Sebastián Ortega** - [SEBCODE](https://github.com/SEBCODE)

## 📄 Licencia

MIT License
