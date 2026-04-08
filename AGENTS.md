# AI Agent Guide — Shift Transport System

## Architecture Overview
Monolithic Spring Boot 3.2.5 application with layered architecture: `Controller → Service → Repository → Entity`. Uses Thymeleaf for server-rendered UI, session-based authentication via phone number (mock for MVP). Data flows from employee requests to manager assignments to driver passenger lists.

Key entities: `User` (roles via flags like `can_drive`, `is_shift_manager`), `TransportEvent` (date+shift combos), `TransportRequest` (employee submissions). See `docs/project-brief.md` for full domain model.

## Critical Workflows
- **Setup**: Manually create PostgreSQL DB `shift_transport`, run `src/main/resources/schema.sql` then `data.sql`. App uses `ddl-auto: none` and `sql.init.mode: always`.
- **Run**: `mvn spring-boot:run` or double-click `run-app.command`. Access at `http://localhost:8080/auth/login`. Logs show SQL due to `show-sql: true`.
- **Debug**: Thymeleaf templates in `src/main/resources/templates/`; errors in `templates/error/`. Controllers redirect based on session attributes (`isManager`, `isDriver`).

## Project Conventions
- **Enums**: PostgreSQL native enums mapped via `@Enumerated(EnumType.STRING)`, `@Column(columnDefinition="enum_name")`, `@Type(PostgreSQLEnumType.class)`. Example: `TransportEvent.status` uses `transport_event_status` type.
- **Entities**: `@CreationTimestamp`/`@UpdateTimestamp` for audit fields. Unique constraints via `@Table(uniqueConstraints=...)`. Soft delete on `User.isActive`.
- **DTOs**: Used for all data transfer; services return DTOs, not entities. Example: `TransportEventService.getAllUpcomingEvents()` returns `List<TransportEventSummaryDto>`.
- **Exceptions**: `BusinessRuleException` for validation (e.g., duplicate requests), `ResourceNotFoundException` for missing entities. Handled globally in `GlobalExceptionHandler`.
- **Auth**: Mock phone-based login in `AuthController`; stores user ID/name in session. Role checks via user flags, not authorities.

## Integration Points
- **Database**: Local PostgreSQL; config in `application.yml`. No external APIs yet.
- **UI**: Thymeleaf with role-specific templates (e.g., `employee/dashboard.html`, `manager/event-form.html`). Static CSS in `resources/static/css/main.css`.
- **Dependencies**: Core Spring starters (Web, Data JPA, Validation, Thymeleaf). PostgreSQL driver + hypersistence-utils for enum support.

Reference: `pom.xml` for deps, `application.yml` for config, `src/main/java/com/shimon/transport/entity/` for JPA patterns, `docs/db-notes.md` for schema decisions.</content>
<parameter name="filePath">/Users/shimonesterkin/Documents/Claude/Projects/Transportation system project/AGENTS.md
