# 🌟 Spring Sec REST API

**Spring RMS REST** is a secure, modern RESTful API built with Spring Boot, designed for resource management with role-based access control. It supports `ADMIN` and `USER` roles, using JWT authentication, PostgreSQL, Swagger UI, and Gmail SMTP for a robust experience.

> ⚙️ **Easily Customizable Template**: This project is designed like a flexible template. Whether you're managing books, products, or any other resource, you can quickly adapt it by updating the resource model. Just change the entity definitions and endpoints accordingly, and you're good to go!

---

## 🚀 Features

- 🔒 **Authentication:** JWT-based login and registration.
- 🛡️ **Authorization:** Role-based access control (ADMIN and USER).
- 📦 **Resource Management:** Full CRUD on resources with audit logging.
- 📅 **Booking System:** Users request bookings; admins approve/reject via email.
- 📜 **API Documentation:** Interactive Swagger UI at `/swagger-ui`.
- 📧 **Email Notifications:** For bookings and resource creation.
- 🗄️ **Database:** PostgreSQL with Flyway migrations.
- 📊 **Audit Logging:** Tracks actions in `audit_logs`.

---

## 🛠️ Tech Stack

| Technology        | Version     |
|-------------------|-------------|
| Spring Boot       | 3.x         |
| Java              | 17          |
| PostgreSQL        | 12+         |
| Spring Security   | JWT-based   |
| Swagger/OpenAPI   | 3.x         |
| Jakarta Mail      | -           |
| Build Tool        | Maven       |

**Key Dependencies:**

- `spring-boot-starter-web`
- `spring-boot-starter-security`
- `spring-boot-starter-data-jpa`
- `spring-boot-starter-mail`
- `springdoc-openapi-starter-webmvc-ui`
- `postgresql`
- `jjwt`
- `lombok`
- `slf4j-api`
