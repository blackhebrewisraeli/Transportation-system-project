# Database Notes — Shift Transport System

**Version:** 1.0
**Database:** PostgreSQL

---

## Design Decisions

### Primary Keys
All tables use `BIGSERIAL` auto-incrementing IDs as primary keys. `phone_number` is the business identifier for users but is not the PK — it is a UNIQUE column.

### Enums
PostgreSQL native `CREATE TYPE ... AS ENUM` is used for all enum columns. On the Java/JPA side, these map to `@Enumerated(EnumType.STRING)` with a custom column definition.

**Important JPA note:** PostgreSQL enums require special handling in Hibernate. The entity must use `@Column(columnDefinition = "user_type")` (matching the PG type name) or a custom `@Type` annotation. This will be addressed when building entities.

### Timestamps
`created_at` and `updated_at` use `TIMESTAMP` with `DEFAULT NOW()`. On the JPA side, `@CreationTimestamp` and `@UpdateTimestamp` (Hibernate annotations) will manage these automatically.

### Unique Constraints
- `users.phone_number` — one user per phone number
- `transport_events (event_date, shift_type_id)` — one event per date+shift
- `transport_requests (user_id, transport_event_id)` — one request per user per event
- `special_dates.special_date` — one entry per date

### Indexes
Added explicit indexes on frequently queried columns: `event_date`, `status`, `assigned_driver_id`, `transport_event_id`, `user_id`.

### Soft Delete
Users are deactivated via `is_active = FALSE`, not deleted. Queries for active users should always filter on `is_active = TRUE`.

### Schema Management
For MVP, `schema.sql` and `data.sql` are run manually or via Spring Boot's initialization (`spring.sql.init.mode=always`). For later phases, consider Flyway or Liquibase for migration management.

---

## How to Run

1. Create the database:
   ```sql
   CREATE DATABASE shift_transport;
   ```

2. Run `schema.sql` to create tables.

3. Run `data.sql` to insert seed data.

Or let Spring Boot handle it via `application.yml`:
```yaml
spring:
  sql:
    init:
      mode: always
```
