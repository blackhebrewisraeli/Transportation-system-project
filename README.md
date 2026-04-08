# 🚐 Shift Transport System

<p align="center">
  <img src="https://img.shields.io/badge/Java-17-blue?style=for-the-badge" alt="Java 17" />
  <img src="https://img.shields.io/badge/Spring_Boot-Backend-brightgreen?style=for-the-badge" alt="Spring Boot" />
  <img src="https://img.shields.io/badge/PostgreSQL-Database-336791?style=for-the-badge" alt="PostgreSQL" />
  <img src="https://img.shields.io/badge/Thymeleaf-Server_Rendered_UI-005F0F?style=for-the-badge" alt="Thymeleaf" />
  <img src="https://img.shields.io/badge/Status-MVP_in_Progress-orange?style=for-the-badge" alt="Status" />
</p>

<p align="center">
  A backend-oriented internal web application for managing employee transportation requests in shift-based operations.
</p>

---

## 📖 Table of Contents

- [📌 Overview](#-overview)
- [❗ Problem Statement](#-problem-statement)
- [🎯 MVP Goals](#-mvp-goals)
- [👥 User Roles](#-user-roles)
- [✨ Current Features](#-current-features)
- [🚧 Not Included Yet](#-not-included-yet)
- [🛠️ Tech Stack](#️-tech-stack)
- [🧱 Project Structure](#-project-structure)
- [🗄️ Database Design](#️-database-design)
- [📍 Current Status](#-current-status)
- [▶️ How to Run](#️-how-to-run)
- [🧭 Roadmap](#-roadmap)
- [📂 Additional Documentation](#-additional-documentation)
- [💡 Notes](#-notes)

---

## 📌 Overview

In shift-based emergency environments, some shifts require workplace-organized transportation because public transportation is unavailable or insufficient.

In many workplaces, this process is handled manually through WhatsApp polls and ad hoc coordination. This project aims to replace that workflow with a structured internal system that supports employees, managers, and drivers.

The system is being built as a practical backend portfolio project with a realistic MVP scope and room for future expansion.

---

## ❗ Problem Statement

The current transportation coordination process is inefficient because:

- employees must notice and respond to WhatsApp polls on time
- staff manually collect responses and prepare driver lists
- information is scattered across chat messages
- there is no structured source of truth
- transportation requests cannot be managed cleanly in advance

---

## 🎯 MVP Goals

The current MVP focuses on four core capabilities:

- employees can log in and submit transport requests
- employees can choose **pickup**, **drop-off**, or **both**
- managers can view requests by date and shift
- drivers can view their assigned transport list

---

## 👥 User Roles

### 👤 Employee
- log in with a phone-based flow
- view relevant transport events
- submit a transport request
- choose pickup, drop-off, or both

### 🧑‍💼 Manager
- view transport events
- review employee requests
- assign drivers
- monitor operational status

### 🚗 Driver
- view assigned transport events
- view passenger lists
- access passenger phone numbers and addresses

---

## ✨ Current Features

- ✅ Spring Boot project structure
- ✅ PostgreSQL schema and seed data
- ✅ JPA entities and repositories
- ✅ transport-event based design
- ✅ initial employee / manager / driver workflow foundation

---

## 🚧 Not Included Yet

- route optimization
- Google Maps / Waze integration
- external calendar sync
- real SMS OTP integration
- advanced validation rules
- production-ready security layer

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Backend | Spring Boot |
| Persistence | Spring Data JPA |
| UI | Thymeleaf |
| Database | PostgreSQL |
| Build Tool | Maven |
| IDE | IntelliJ IDEA |
| DB Tool | DataGrip |

---

## 🧱 Project Structure

```text
src/main/java/com/shimon/transport/
├── controller
├── dto
├── entity
├── enums
├── exception
├── repository
├── service
```

---

## 🗄️ Database Design

The current design is centered around these main entities:

- `users`
- `shift_types`
- `transport_events`
- `transport_requests`
- `special_dates` *(planned / partial)*

---

## 📍 Current Status

Progress so far:

- [x] Project brief
- [x] Initial database schema
- [x] Seed data
- [x] Spring Boot project structure
- [x] Initial JPA entities and repositories
- [ ] Employee flow polishing
- [ ] Manager assignment flow polishing
- [ ] Driver assigned-list flow polishing
- [ ] Validation improvements
- [ ] Authentication hardening

---

## ▶️ How to Run

### 1️⃣ Create the database

Create a PostgreSQL database, for example:

```sql
CREATE DATABASE shift_transport;
```

### 2️⃣ Run SQL setup

Execute the following files on the target database:

- `schema.sql`
- `data.sql`

You can run them using PostgreSQL or DataGrip.

### 3️⃣ Configure the application

Update `src/main/resources/application.yml` with your local PostgreSQL credentials.

Recommended settings for manual SQL setup:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/shift_transport
    username: YOUR_USERNAME
    password: YOUR_PASSWORD

  jpa:
    hibernate:
      ddl-auto: validate

  sql:
    init:
      mode: never
```

### 4️⃣ Start the application

Run:

```bash
mvn spring-boot:run
```

Or start `ShiftTransportApplication` directly from IntelliJ IDEA.

### 5️⃣ Open in browser

Visit:

```text
http://localhost:8080/auth/login
```

---

## 🧭 Roadmap

### Near-term
- improve employee request flow
- complete manager driver-assignment flow
- complete driver assigned-list flow
- add validation for conflicting requests

### Later
- special date automation
- real SMS OTP authentication
- navigation integration
- better operational reporting
- calendar-based transport event creation

---

## 📂 Additional Documentation

```text
docs/
├── project-brief.md
├── db-notes.md
├── first-run-checklist.md
```

---

## 💡 Notes

This project is being built incrementally as a practical backend portfolio project, with emphasis on:

- clean project structure
- realistic MVP scope
- maintainable backend design
- future extensibility