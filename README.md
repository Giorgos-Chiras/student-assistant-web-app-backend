# Student Assistant Web App Backend

This is the backend for the **Student Assistant Web App**, built with **Spring Boot**. It helps students manage courses, tasks, professors, in a structured way. The backend provides a secure REST API with full CRUD operations and follows best practices for exception handling and testing.

---

## Features

- **User Management**: Register, authenticate, and manage your student account
- **Courses & Professors**: Manage courses and assign professors.
- **Tasks**: CRUD operations for tasks, including deadlines and status tracking.
- **Security**: Secured endpoints using **Spring Security**.
- **Database**: Relational database support with **H2** for development.
- **DTOs**: Use of **Lombok** to reduce boilerplate code.
- **Full REST API**: Supports **POST**, **GET**, **PUT**, **DELETE** requests.
- **Exception Handling**: Centralized handling of errors and custom exceptions.
- **Testing**: Unit tests using **JUnit 5**.

---

## Technologies

- Java 17+  
- Spring Boot  
- Spring Security  
- H2 Database  
- Maven  
- Lombok  
- JUnit 5  

---

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- Git

## API Endpoints

The backend exposes RESTful endpoints for managing **Users**, **Professors**, **Courses**, and **Tasks**.  
All endpoints are **secured** and require authentication.

---

### 👤 Users
| Method | Endpoint                | Description                  |
|--------|-------------------------|------------------------------|
| GET    | `/users/{id}`           | Get user by ID              |
| GET    | `/users/username/{username}` | Get user by username   |
| POST   | `/users`                | Create a new user (with password validation) |
| PUT    | `/users/{id}`           | Update user details         |
| PUT    | `/users/{id}/password`  | Update user password        |
| PUT    | `/users/{id}/email`     | Update user email           |
| DELETE | `/users/{id}`           | Delete user                 |

---

### 🎓 Professors
| Method | Endpoint           | Description            |
|--------|-------------------|------------------------|
| GET    | `/professors/{id}` | Get professor by ID   |
| GET    | `/professors`      | Get all professors    |
| POST   | `/professors`      | Create a new professor |
| PUT    | `/professors/{id}` | Update professor       |
| DELETE | `/professors/{id}` | Delete professor       |

---

### 📚 Courses
| Method | Endpoint         | Description           |
|--------|-----------------|-----------------------|
| GET    | `/courses/{id}` | Get course by ID      |
| GET    | `/courses`      | Get all courses       |
| POST   | `/courses`      | Create a new course   |
| PUT    | `/courses/{id}` | Update a course       |
| DELETE | `/courses/{id}` | Delete a course       |

---

### ✅ Tasks
| Method | Endpoint       | Description           |
|--------|---------------|-----------------------|
| GET    | `/tasks/{id}` | Get task by ID        |
| GET    | `/tasks`      | Get all tasks         |
| POST   | `/tasks`      | Create a new task     |
| PUT    | `/tasks/{id}` | Update a task         |
| DELETE | `/tasks/{id}` | Delete a task         |

---

### ⚠️ Exception Handling
All controllers include centralized exception handling with meaningful HTTP status codes:

- `403 FORBIDDEN` → Access denied or resource not found  
- `409 CONFLICT` → Duplicate entries (e.g., user/email conflict)  
- `400 BAD REQUEST` → Invalid request data (e.g., password validation fails)  




