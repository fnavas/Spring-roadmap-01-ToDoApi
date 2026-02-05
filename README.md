# Task Manager API (Spring Roadmap #01)

[![Java Version](https://img.shields.io/badge/Java-17%2B-orange)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.x-brightgreen)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

## 📌 Project Overview
This is the first project of my **Spring Boot Mastery Roadmap**. It is a professional-grade RESTful API for managing tasks (To-Do list). The goal of this project was to implement the core fundamentals of Spring Boot, focusing on clean code, proper layered architecture, and global exception handling.

## 🚀 Key Features
* **Full CRUD Operations**: Create, Read, Update, and Delete tasks.
* **Data Validation**: Strict input validation using `jakarta.validation`.
* **Global Exception Handling**: Centralized error management returning consistent JSON responses.
* **Automated Data Seeding**: Pre-populated database for testing purposes (via `data.sql`).
* **Interactive API Docs**: Built-in Swagger UI for easy endpoint testing.

## 🛠️ Tech Stack
* **Java 17**
* **Spring Boot 4.x**
* **Spring Data JPA**
* **H2 Database** (In-memory for easy testing)
* **Jakarta Validation**
* **MapStruct 1.6.x** (for DTO mapping)
* **Lombok**
* **JUnit 5 & Mockito (Unit testing with 80%+ coverage)**
* **SpringDoc OpenAPI (Swagger)**

## 🏗️ Architecture
This project follows a **Layered Architecture**:
1.  **Controller Layer**: Handles HTTP requests and mapping.
2.  **Service Layer**: Contains the business logic (decoupled from the API).
3.  **Repository Layer**: Manages data persistence using JPA.
4.  **DTOs**: Data Transfer Objects to ensure internal entities are not exposed.

## 🚦 Getting Started

### Prerequisites
* JDK 17 or higher
* Maven 3.6+

### Installation & Run
1. Clone the repository:
   ```bash
   git clone [https://github.com/](https://github.com/)[TU_USUARIO]/spring-roadmap-01-todoapi.git
2. Navigate to the folrder:
   ```bash
   cd spring-roadmap-01-todoapi
3. Run the application:
   ```bash
   ./mvw spring-boot:run
The server will start at `http://localhost:8080`.

## 📖 API Documentation

Once the application is running, you can explore and test the endpoints via Swagger UI: 👉 
[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

## 🧪 Example Endpoints

* `GET /api/v1/tasks` - Retrieve all tasks.
* `POST /api/v1/tasks` - Create a new task.
* `GET /api/v1/tasks/{id}` - Get task details.
* `PUT /api/v1/tasks/{id}` - Update an existing task.
* `DELETE /api/v1/tasks/{id}` - Remove a task.   
