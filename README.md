# Enterprise Task Management API

A Spring Boot REST API for managing users, projects, and tasks with JWT authentication, auditing, and Swagger docs. Designed for teams and organizations; production-ready foundation with Docker and CI-friendly setup.

## Features

- Authentication: JWT login/register, role-based access (Admin, Manager, User)
- Users: CRUD, activate/deactivate
- Projects: CRUD
- Tasks: CRUD, assignment, status transitions (TODO → IN_PROGRESS → DONE), priorities
- Auditing: Records who/what/when changes
- Docs: Swagger UI + OpenAPI
- DevOps: Dockerfile + docker-compose
- Tests: Unit tests with JUnit + Mockito

## Tech Stack

- Java 21, Spring Boot 3
- Spring Web, Security, Data JPA, Validation
- PostgreSQL (Supabase-ready)
- JUnit 5, Mockito
- Docker, docker-compose
- Swagger (springdoc-openapi)

## Architecture

- Layered: Controller → Service → Repository
- Entities:
  - User: `app_user(id uuid, name, email, role, password_hash, active, created_at, updated_at)`
  - Project: `project(id uuid, name, description, start_date, end_date, created_at, updated_at)`
  - Task: `task(id uuid, title, description, status, priority, project_id, assignee_id, created_at, updated_at)`
  - AuditLog: `audit_log(id uuid, entity, entity_id, action, details, performed_by, performed_at)`

## Getting Started

### Prerequisites
- Java 21
- Maven 3.9+
- PostgreSQL (or Supabase)
- Docker (optional for containerized run)

### Configuration

Set DB and JWT in `src/main/resources/application.properties` or via environment variables:

Required properties:
- `spring.datasource.url` (e.g., `jdbc:postgresql://<host>:5432/<db>?sslmode=require` for Supabase)
- `spring.datasource.username`
- `spring.datasource.password`
- `app.security.jwt.secret` (Base64 string)
- `app.security.jwt.expiration-ms` (e.g., `3600000`)

Local Postgres (docker-compose) values are already wired via env:
- `DB_URL=jdbc:postgresql://db:5432/task_management`
- `DB_USERNAME=postgres`
- `DB_PASSWORD=postgres`

### Run locally (Maven)

From project root, then into the app directory:

```bash
# Windows PowerShell
cd Task-management
.\mvnw.cmd clean package
java -jar target\Task-management-0.0.1-SNAPSHOT.jar
```

App runs at:
- API: http://localhost:8080
- Swagger: http://localhost:8080/swagger-ui.html
- Health: http://localhost:8080/health

### Run with Docker (recommended)

```bash
# From Task-management directory (where Dockerfile and docker-compose.yml live)
docker compose up --build
```

- API: http://localhost:8080
- Local Postgres exposed on 5432

## API Usage

### Swagger UI
- Browse and test endpoints at: http://localhost:8080/swagger-ui.html

### Auth Endpoints
- Register: `POST /api/auth/register`
- Login: `POST /api/auth/login`

Example:

```bash
# Register
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"name":"Alice","email":"alice@example.com","password":"password123"}'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"alice@example.com","password":"password123"}'
```

Use the `token` returned from login with:
`Authorization: Bearer <JWT>`

### Users (requires JWT)
- GET `/api/users`
- GET `/api/users/{id}`
- POST `/api/users` (body: `UserDto`)
- PUT `/api/users/{id}` (body: `UserDto`)
- DELETE `/api/users/{id}`
- POST `/api/users/{id}/activate`
- POST `/api/users/{id}/deactivate`

`UserDto` shape:
```json
{
  "id": "uuid (optional on create)",
  "name": "string",
  "email": "string",
  "role": "ADMIN|MANAGER|USER",
  "active": true
}
```

### Projects (requires JWT)
- GET `/api/projects`
- GET `/api/projects/{id}`
- POST `/api/projects` (body: `ProjectDto`)
- PUT `/api/projects/{id}` (body: `ProjectDto`)
- DELETE `/api/projects/{id}`

`ProjectDto` shape:
```json
{
  "id": "uuid (optional)",
  "name": "string",
  "description": "string",
  "startDate": "YYYY-MM-DD",
  "endDate": "YYYY-MM-DD"
}
```

### Tasks (requires JWT)
- GET `/api/tasks`
- GET `/api/tasks/{id}`
- POST `/api/tasks` (body: `TaskDto`)
- PUT `/api/tasks/{id}` (body: `TaskDto`)
- DELETE `/api/tasks/{id}`
- POST `/api/tasks/{id}/transition/{status}` (status: `TODO|IN_PROGRESS|DONE`)

`TaskDto` shape:
```json
{
  "id": "uuid (optional)",
  "title": "string",
  "description": "string",
  "status": "TODO|IN_PROGRESS|DONE",
  "priority": "LOW|MEDIUM|HIGH",
  "projectId": "uuid",
  "assigneeId": "uuid (optional)"
}
```

Status workflow enforced:
- `TODO -> IN_PROGRESS -> DONE`

## Testing

Run unit tests:
```bash
cd Task-management
.\mvnw.cmd -q test
```

- Tests cover services (User, Project, Task) with Mockito.

## Docker

- `Dockerfile`: Multi-stage build (Maven build + JRE runtime)
- `docker-compose.yml`: App + PostgreSQL for local development

Helpful:
```bash
docker compose up --build
docker compose down -v
```

## CI/CD

-  GitHub Actions workflow:
  - Build + Test on push
  - Build/push Docker image
  - Deploy to Render (or other platform)

## Notes

- DB schema is aligned for UUID primary keys (Supabase friendly).
- Auditing recorded via `AuditLog` entity in service layer.
- For production, set strong `app.security.jwt.secret` (Base64) and disable Swagger if needed.

## License

MIT (or your preferred license).
