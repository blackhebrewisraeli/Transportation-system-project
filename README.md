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
🗄️ Database Design

The current design is centered around these main entities:

users
shift_types
transport_events
transport_requests
special_dates (planned / partial)
📍 Current Status

Progress so far:

 Project brief
 Initial database schema
 Seed data
 Spring Boot project structure
 Initial JPA entities and repositories
 Employee flow polishing
 Manager assignment flow polishing
 Driver assigned-list flow polishing
 Validation improvements
 Authentication hardening
▶️ How to Run
1️⃣ Create the database

Create a PostgreSQL database, for example:

CREATE DATABASE shift_transport;
2️⃣ Run SQL setup

Execute the following files on the target database:

schema.sql
data.sql

You can run them using PostgreSQL or DataGrip.

3️⃣ Configure the application

Update src/main/resources/application.yml with your local PostgreSQL credentials.

Recommended settings for manual SQL setup:

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
4️⃣ Start the application

Run:

mvn spring-boot:run

Or start ShiftTransportApplication directly from IntelliJ IDEA.

5️⃣ Open in browser

Visit:

http://localhost:8080/auth/login
🧭 Roadmap
Near-term
improve employee request flow
complete manager driver-assignment flow
complete driver assigned-list flow
add validation for conflicting requests
Later
special date automation
real SMS OTP authentication
navigation integration
better operational reporting
calendar-based transport event creation
📂 Additional Documentation
docs/
├── project-brief.md
├── db-notes.md
├── first-run-checklist.md
💡 Notes

This project is being built incrementally as a practical backend portfolio project, with emphasis on:

clean project structure
realistic MVP scope
maintainable backend design
future extensibility
