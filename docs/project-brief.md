# Project Brief — Shift Transport System

**Version:** 1.0
**Date:** 2026-04-07
**Author:** Shimon
**Status:** Draft — MVP Planning

---

## 1. System Definition

The **Shift Transport System** is an internal web application for coordinating employee transportation during shift-based work at an emergency call center.

The system replaces a manual, WhatsApp-based coordination process with a structured, backend-driven workflow that allows employees to register transportation needs in advance, gives managers operational visibility, and provides drivers with clean execution lists.

---

## 2. Problem Statement

During weekends, holidays, and other exceptional periods, public transportation is unavailable or insufficient for employees to reach their shifts on time. The workplace currently uses a WhatsApp group to manage this, where a staff member posts a poll the day before each relevant shift, collects responses manually, and prepares a driver list by hand.

This process is unreliable because:

- Employees miss polls or respond late
- There is no structured record of who needs transport
- Staff must repeat the same manual work every relevant shift
- Drivers receive lists prepared informally, with no single source of truth
- Managers have no operational overview
- Special periods (holidays, emergencies, 12-hour shifts) are harder to handle manually

---

## 3. Product Vision

An internal, structured system where employees proactively register transportation needs, managers oversee and coordinate, and drivers receive a clean, ready-to-execute passenger list — without relying on WhatsApp polls.

The system starts as a minimal viable product and is designed to grow incrementally into a fuller operational tool.

---

## 4. MVP Scope

The first version delivers the following and nothing more:

- Employee can log in and view open transport events
- Employee can submit a transport request (pickup / drop-off / both)
- Employee can cancel or update a future request
- Manager can view all requests per date and shift
- Manager can assign a driver to a transport event
- Driver can view events assigned to them and the final passenger list (names, addresses, phone numbers)
- Basic calendar logic: system knows which dates and shifts are transport-relevant (Fridays, Saturdays)

---

## 5. Out of Scope for MVP

The following are explicitly **not** part of the first version:

- Route optimization or navigation assistance
- WhatsApp bot or WhatsApp integration
- External calendar sync (Google Calendar, etc.)
- Real SMS OTP with a production provider
- Complex holiday/wartime rule engine
- Mobile app
- Advanced conflict detection between adjacent shifts

---

## 6. User Roles

### Employee
A regular shift worker who may need transport.

Capabilities:
- Log in via phone number
- View open transport events
- Submit a transport request (PICKUP / DROPOFF / BOTH)
- Update or cancel a future request (before the event is locked)
- View their own upcoming transport status

Constraint: only users with `can_request_transport = true` may submit requests.

### Driver
A person who drives the workplace vehicle. May be a regular employee, a retiree, or a volunteer.

Capabilities:
- Log in via phone number
- View transport events assigned to them
- View the passenger list for each assigned event (name, address, phone number)

Constraint: only users with `can_drive = true` may be assigned as a driver.
If the driver is also a regular employee (`user_type = EMPLOYEE`), they may also submit passenger requests for themselves.
If the driver is only a volunteer or retiree, passenger-request functionality is hidden.

### Manager / Shift Manager
The operational supervisor responsible for running transport coordination.

Capabilities:
- View all transport events
- View all requests per event
- Assign a driver to an event
- Monitor overall transport demand
- Mark events as open, locked, or cancelled
- Manage special dates

### System Admin
Higher-privilege technical role.

Capabilities:
- Manage user accounts (create, deactivate, update roles/flags)
- Configure system settings
- Access all views

---

## 7. Core Entities

| Entity | Description |
|---|---|
| `User` | Any person in the system — employee, driver, manager, admin |
| `ShiftType` | Named shift with start/end times (MORNING, AFTERNOON, NIGHT) |
| `TransportEvent` | A specific date + shift combination requiring transport coordination |
| `TransportRequest` | A single employee's request to be included in a specific event |
| `SpecialDate` | A manually or rule-defined date with transport relevance (holiday, wartime, etc.) |

---

## 8. User Type Definitions

| Value | Description |
|---|---|
| `EMPLOYEE` | Regular shift worker |
| `VOLUNTEER` | External volunteer driver — not an employee |
| `RETIREE` | Retired person acting as driver — not an employee |
| `STAFF` | Non-shift administrative or support staff |

---

## 9. Key Enums

| Enum | Values |
|---|---|
| `UserType` | EMPLOYEE, VOLUNTEER, RETIREE, STAFF |
| `ShiftName` | MORNING, AFTERNOON, NIGHT |
| `RequestDirection` | PICKUP, DROPOFF, BOTH |
| `RequestStatus` | PENDING, APPROVED, CANCELLED |
| `TransportEventStatus` | OPEN, LOCKED, CANCELLED, COMPLETED |
| `SpecialDateType` | HOLIDAY, HOLIDAY_EVE, WARTIME, CUSTOM |

---

## 10. Database Schema (Overview)

### `users`
| Column | Type | Notes |
|---|---|---|
| id | BIGINT PK | Internal primary key |
| full_name | VARCHAR | Required |
| phone_number | VARCHAR UNIQUE | Main business identifier |
| email | VARCHAR | Optional |
| address_text | TEXT | Required for employees who may request transport |
| user_type | ENUM | EMPLOYEE / VOLUNTEER / RETIREE / STAFF |
| can_request_transport | BOOLEAN | Controls passenger functionality |
| can_drive | BOOLEAN | Controls driver assignment |
| is_shift_manager | BOOLEAN | Controls manager view |
| is_system_admin | BOOLEAN | Controls admin access |
| is_active | BOOLEAN | Soft-delete / deactivation flag |
| created_at | TIMESTAMP | |
| updated_at | TIMESTAMP | |

### `shift_types`
| Column | Type | Notes |
|---|---|---|
| id | BIGINT PK | |
| name | ENUM | MORNING / AFTERNOON / NIGHT |
| start_time | TIME | |
| end_time | TIME | |
| is_active | BOOLEAN | |

### `transport_events`
| Column | Type | Notes |
|---|---|---|
| id | BIGINT PK | |
| event_date | DATE | |
| shift_type_id | FK → shift_types | |
| transport_required | BOOLEAN | |
| status | ENUM | OPEN / LOCKED / CANCELLED / COMPLETED |
| assigned_driver_id | FK → users (nullable) | |
| notes | TEXT | |
| created_at | TIMESTAMP | |
| updated_at | TIMESTAMP | |
| UNIQUE | (event_date, shift_type_id) | One event per date+shift |

### `transport_requests`
| Column | Type | Notes |
|---|---|---|
| id | BIGINT PK | |
| user_id | FK → users | |
| transport_event_id | FK → transport_events | |
| direction | ENUM | PICKUP / DROPOFF / BOTH |
| status | ENUM | PENDING / APPROVED / CANCELLED |
| created_at | TIMESTAMP | |
| updated_at | TIMESTAMP | |
| UNIQUE | (user_id, transport_event_id) | One request per user per event |

### `special_dates`
| Column | Type | Notes |
|---|---|---|
| id | BIGINT PK | |
| special_date | DATE | |
| date_type | ENUM | HOLIDAY / HOLIDAY_EVE / WARTIME / CUSTOM |
| description | VARCHAR | |
| transport_relevant | BOOLEAN | |
| created_at | TIMESTAMP | |

---

## 11. Architecture

**Stack:**
- Java 17
- Spring Boot (Web, Data JPA, Validation)
- Thymeleaf (server-rendered UI)
- PostgreSQL
- Spring Security (Phase 2)

**Pattern:** Monolith with layered architecture.

```
Controller → Service → Repository → Entity
                ↓
              DTOs / Mappers
                ↓
            Thymeleaf Templates
```

**Project structure:**
```
src/main/java/com/shimon/transport/
  config/
  controller/
  dto/
  entity/
  enums/
  exception/
  repository/
  service/
  validation/
  mapper/

src/main/resources/
  templates/
  static/css/
  static/js/
  application.yml
  schema.sql
  data.sql

docs/
  project-brief.md
  mvp-scope.md
  business-rules.md
  db-notes.md
```

---

## 12. Main User Flows (MVP)

### Flow 1 — Employee Submits a Transport Request
1. Employee logs in (phone-based, mock OTP for MVP)
2. Employee sees a list of upcoming open transport events
3. Employee selects an event and chooses PICKUP / DROPOFF / BOTH
4. System validates: user is active, `can_request_transport = true`, event is OPEN, no duplicate request exists
5. Request is saved with status PENDING
6. Employee sees confirmation and their upcoming requests

### Flow 2 — Manager Assigns a Driver
1. Manager logs in
2. Manager views transport events (filterable by date, shift)
3. Manager selects an event and sees current request list
4. Manager assigns a driver from a list of active, eligible users (`can_drive = true`)
5. System validates: driver is active, driver not already assigned to a conflicting event (Phase 2)
6. Event is updated with `assigned_driver_id`

### Flow 3 — Driver Views Passenger List
1. Driver logs in
2. Driver sees events assigned to them
3. Driver selects an event and views the final passenger list
4. List shows: full name, phone number, address, direction (PICKUP / DROPOFF / BOTH)

---

## 13. Business Rules (MVP)

**Users:**
- `phone_number` is required and must be unique
- A user without `can_request_transport = true` cannot submit a transport request
- A user without `can_drive = true` cannot be assigned as a driver
- Inactive users (`is_active = false`) cannot log in and cannot be assigned as drivers

**Transport Events:**
- Only one event may exist per `(event_date, shift_type_id)` combination
- A transport event must have status `OPEN` to accept new requests

**Transport Requests:**
- A user may not have more than one request per transport event
- A request can only be cancelled before the event status changes to LOCKED or COMPLETED

**Drivers:**
- Only active, `can_drive = true` users may be assigned as drivers
- Driver who is not of `user_type = EMPLOYEE` should not see or access passenger-request functionality

---

## 14. Business Rules — Planned for Later Phases

- Block adjacent/conflicting shift assignments for drivers
- Enforce request submission deadline (e.g., X hours before shift)
- Auto-generate transport events for Fridays and Saturdays
- Auto-generate transport events for holidays based on special_dates
- Notify manager when an approaching transport-relevant date has no assigned driver

---

## 15. Phased Roadmap

### MVP (Phase 1)
- User entity and basic roles
- Shift types
- Transport events (manual creation by manager)
- Transport requests (employee flow)
- Driver view (passenger list per event)
- Manager dashboard (requests per event, driver assignment)
- Mock OTP login
- Basic calendar logic (Fridays and Saturdays flagged as relevant)
- Thymeleaf UI for all 6 screens

### Phase 2 — Hardening
- Spring Security with session-based auth
- Real OTP via SMS provider
- Adjacent shift conflict detection for driver assignment
- Request cancellation deadline enforcement
- Special dates management UI

### Phase 3 — Calendar Intelligence
- Auto-creation of transport events for known relevant dates
- Holiday/eve detection using special_dates table
- Manager alerts for upcoming dates without a driver assigned
- Wartime / exceptional period mode

### Phase 4 — Navigation & Reporting
- Address-based passenger list with external map links
- Route suggestion (open in Waze / Google Maps per address)
- Operational reports (requests per period, driver usage, etc.)
- Possible API layer for future mobile app or WhatsApp bot

---

## 16. Transport-Relevant Dates (Routine Mode)

| Day/Shift | Relevant? |
|---|---|
| Friday morning | Yes |
| Friday afternoon | Configurable |
| Friday night | Yes |
| Saturday morning | Yes |
| Saturday afternoon | Yes |
| Saturday night | Yes |
| Other days | Generally no (unless special date) |

---

## 17. Authentication (MVP Approach)

- Users identify by phone number
- OTP is sent via SMS (mocked in MVP — code printed to console or returned in response)
- No password is stored
- Session management deferred to Phase 2 (Spring Security)
- For MVP: simple session token or hardcoded dev bypass is acceptable

---

## 18. Non-Goals and Privacy Constraints

- Do **not** collect national ID numbers
- Minimize personal data to: full name, phone number, address, optional email
- No GPS tracking, no biometric data, no salary data
- Address is stored as free text (`address_text`) — no structured geocoding in MVP

---

*End of project-brief.md v1.0*
