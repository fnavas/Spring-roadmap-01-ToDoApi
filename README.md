# Task Manager API (Spring Roadmap #01)

[![Java Version](https://img.shields.io/badge/Java-17%2B-orange)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.x-brightgreen)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

## Project Overview

This is the first project of my **Spring Boot Mastery Roadmap**. It is a RESTful API for managing tasks (To-Do list), built to practice the core fundamentals of Spring Boot: clean layered architecture, input validation, dynamic filtering, pagination, environment profiles, and production-ready monitoring.

## Key Features

- **Full CRUD**: Create, read, update, and delete tasks.
- **Dynamic Filtering**: Filter tasks by title, description, completion status, and priority.
- **Pagination & Sorting**: All list endpoints support page, size, sortBy, and sortDir parameters.
- **Input Validation**: Request body and path variable validation via `jakarta.validation` (`@NotBlank`, `@Positive`, `@FutureOrPresent`).
- **Global Exception Handling**: Centralized error responses with consistent JSON structure.
- **Environment Profiles**: Separate configuration for `dev`, `test`, and `prod` environments.
- **Health & Monitoring**: Spring Boot Actuator endpoints (`/actuator/health`, `/actuator/info`).
- **API Documentation**: Interactive Swagger UI via SpringDoc OpenAPI.
- **Docker Support**: Multi-stage Dockerfile for containerized deployment.

## Tech Stack

| Layer | Technology |
|-------|------------|
| Language | Java 17 |
| Framework | Spring Boot 4.x |
| Persistence | Spring Data JPA + H2 |
| Mapping | MapStruct 1.6.x |
| Validation | Jakarta Validation |
| Documentation | SpringDoc OpenAPI (Swagger) |
| Monitoring | Spring Boot Actuator |
| Testing | JUnit 5 + Mockito (34 tests) |
| Utilities | Lombok |

## Architecture

Layered architecture with clear separation of concerns:

```
Controller  →  Service  →  Repository  →  Database
    ↕              ↕
   DTOs         Mapper (MapStruct)
```

- **Controller**: HTTP request/response handling and validation.
- **Service**: Business logic, dynamic filtering via JPA `Specification`.
- **Repository**: Data access (`JpaRepository` + `JpaSpecificationExecutor`).
- **DTOs**: Internal entities never exposed directly to the API.
- **Exception Handler**: `@RestControllerAdvice` returning structured `ErrorResponse`.

## Getting Started

### Prerequisites

- JDK 17 or higher
- Maven 3.6+

### Run

```bash
git clone https://github.com/fnavas/spring-roadmap-01-todoapi.git
cd spring-roadmap-01-todoapi
./mvnw spring-boot:run
```

The server starts at `http://localhost:8080` using the `dev` profile by default (H2 in-memory, Swagger enabled, Actuator fully exposed).

### Run with a specific profile

```bash
# Development (default)
./mvnw spring-boot:run

# Production
SPRING_PROFILES_ACTIVE=prod ./mvnw spring-boot:run

# Docker
docker build -t todoapi .
docker run -e SPRING_PROFILES_ACTIVE=prod -p 8080:8080 todoapi
```

### Run tests

```bash
./mvnw test
```

Tests run with the `test` profile automatically (isolated H2 database, no sample data loaded).

## API Reference

### Endpoints

| Method | URL | Description |
|--------|-----|-------------|
| `GET` | `/api/v1/tasks` | List tasks (filterable, paginated) |
| `GET` | `/api/v1/tasks/{id}` | Get task by ID |
| `POST` | `/api/v1/tasks` | Create a task |
| `PUT` | `/api/v1/tasks/{id}` | Update a task |
| `DELETE` | `/api/v1/tasks/{id}` | Delete a task |

### Query Parameters for `GET /api/v1/tasks`

| Parameter | Type | Description | Example |
|-----------|------|-------------|---------|
| `title` | String | Filter by title (case-insensitive, partial match) | `title=groceries` |
| `description` | String | Filter by description (case-insensitive, partial match) | `description=milk` |
| `completed` | Boolean | Filter by completion status | `completed=false` |
| `priority` | Enum | Filter by priority (`LOW`, `MEDIUM`, `HIGH`) | `priority=HIGH` |
| `page` | Integer | Page number (0-based, default: 0) | `page=1` |
| `size` | Integer | Page size (default: 10) | `size=20` |
| `sortBy` | String | Field to sort by (default: `id`) | `sortBy=dueDate` |
| `sortDir` | String | Sort direction: `asc` or `desc` (default: `asc`) | `sortDir=desc` |

### Request Body for `POST` / `PUT`

```json
{
  "title": "Buy groceries",
  "description": "Milk, eggs, bread",
  "completed": false,
  "priority": "HIGH",
  "dueDate": "2026-12-31"
}
```

### Task fields

| Field | Type | Constraints |
|-------|------|-------------|
| `id` | Long | Read-only, auto-generated |
| `title` | String | Required, max 100 chars |
| `description` | String | Optional, max 255 chars |
| `completed` | Boolean | Defaults to `false` |
| `priority` | Enum | `LOW`, `MEDIUM`, `HIGH` — defaults to `MEDIUM` |
| `dueDate` | Date (ISO) | Optional, must be today or future |
| `created` | Date (ISO) | Read-only, set on creation |

### Error Response

All errors return a consistent JSON structure:

```json
{
  "statusCode": 404,
  "message": "Task not found",
  "error": "Task with id 99 not found"
}
```

## API Documentation

Swagger UI (available in `dev` profile):
[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

## Monitoring

Actuator endpoints (available in all profiles):

| Endpoint | Dev | Prod |
|----------|-----|------|
| `/actuator/health` | Full details | Status only |
| `/actuator/info` | ✅ | ✅ |
| `/actuator/metrics` | ✅ | ❌ |
| `/actuator/env` | ✅ | ❌ |
