# bookingApp

## Overview
This setup includes:
- **Keycloak** for authentication and authorization.
- **PostgreSQL** for the application database.
- **Spring Boot** application accessible with a Bearer Token.

---

## Services and Access Details

### Keycloak
- **URL**: [http://localhost:8081](http://localhost:8081)
- **Admin Credentials**:
    - Username: `admin`
    - Password: `admin`
- **Users**:
    - `book-user` / `book-user` (standard user)
    - `book-admin` / `book-admin` (admin user)

### Spring Boot Application
- **URL**: [http://localhost:8080](http://localhost:8080)
- **Authentication**: Bearer Token from Keycloak required.

### PostgreSQL Database
- **Host**: `localhost`
- **Port**: `5432`
- **Database Name**: `booking_app`
- **Credentials**:
    - Username: `frasoprano`
    - Password: `frasoprano`

---
### Swagger UI
- **URL**: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

## Run Instructions
Navigate to the `src/main/resources/docker` directory:
```bash
podman compose up -d // docker compose up -d