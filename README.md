# MILO Spring Boot Backend 🍃⚡

Production Spring Boot REST API service for the **MILO Social Community Platform** (supporting hyper-local Pune communities like Wakad, Baner, Hinjewadi).

---

## 🛠️ Tech Stack & Prerequisites

- **Framework**: Spring Boot 3.2.4 (Java 17)
- **Security**: Spring Security & JWT Token Authentication
- **Database**: PostgreSQL (Supabase / Neon DB compatible)
- **ORM**: Spring Data JPA / Hibernate
- **Build Tool**: Maven

---

## 📂 Project Architecture

```
backend/
├── schema.sql                           # Complete PostgreSQL DDL & Seed Script
├── pom.xml                              # Maven Configuration & Dependencies
├── src/main/resources/
│   └── application.yml                  # Database URL, Connection Pool & JWT Config
└── src/main/java/com/milo/
    ├── MiloApplication.java             # Main Application Entrypoint
    ├── config/
    │   └── SecurityConfig.java          # CORS & Endpoint Security Rules
    ├── controller/
    │   ├── AuthController.java          # Registration & Authentication APIs
    │   ├── UserController.java          # Profile & User Management APIs
    │   ├── EventController.java         # Activity Hosting & Joining APIs
    │   ├── ActivityController.java      # Category & Activity Catalog APIs
    │   ├── GroupController.java         # Community Discussions & Groups APIs
    │   ├── ConnectionController.java    # Friend Connections & Network APIs
    │   └── AdminController.java         # Moderation & Platform Administration APIs
    ├── model/                           # JPA Entities (User, Event, Activity, Group, Connection, Report)
    └── repository/                      # Spring Data JPA Repositories
```

---

## 🗄️ Database Setup (PostgreSQL / Neon)

1. Open your PostgreSQL / Neon SQL Console.
2. Execute the provided script: [`schema.sql`](./schema.sql).
3. The script will automatically wipe old tables, create performance indexes, and populate initial seed data for Pune members, activities, and events.

---

## 🚦 API Endpoints

### 🔑 Authentication (`/api/auth`)
- `POST /api/auth/register`: Create a new user account.
- `POST /api/auth/login`: Authenticate and receive JWT token.

### 👤 User Management (`/api/users`)
- `GET /api/users`: Fetch all active Pune members.
- `GET /api/users/{id}`: Fetch user profile details.
- `PUT /api/users/{id}`: Update profile, bio, location, interests.

### 🎯 Events & Plans (`/api/events`)
- `GET /api/events`: Fetch approved community events.
- `POST /api/events`: Create a new event or meetup.
- `POST /api/events/{id}/join`: Join an upcoming event.

### 🛡️ Admin Suite (`/api/admin`)
- `GET /api/admin/stats`: Get system-wide platform statistics.
- `GET /api/admin/users`: Manage user permissions & moderation (BLOCK / UNBLOCK).
- `GET /api/admin/reports`: View reported posts & open tickets.

---

## 🏃 Running locally

1. Configure database credentials in `src/main/resources/application.yml` or set environment variables.
2. Build and run:
   ```bash
   mvn spring-boot:run
   ```
3. Server starts at: `http://localhost:8080/api`
