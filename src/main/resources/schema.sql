-- ============================================================
-- Shift Transport System — Initial Database Schema (MVP)
-- Version: 1.0
-- Database: PostgreSQL
-- ============================================================

-- ============================================================
-- ENUM TYPES
-- ============================================================

CREATE TYPE user_type AS ENUM ('EMPLOYEE', 'VOLUNTEER', 'RETIREE', 'STAFF');
CREATE TYPE shift_name AS ENUM ('MORNING', 'AFTERNOON', 'NIGHT');
CREATE TYPE request_direction AS ENUM ('PICKUP', 'DROPOFF', 'BOTH');
CREATE TYPE request_status AS ENUM ('PENDING', 'APPROVED', 'CANCELLED');
CREATE TYPE transport_event_status AS ENUM ('OPEN', 'LOCKED', 'CANCELLED', 'COMPLETED');
CREATE TYPE special_date_type AS ENUM ('HOLIDAY', 'HOLIDAY_EVE', 'WARTIME', 'CUSTOM');

-- ============================================================
-- TABLE: users
-- ============================================================
-- Central user table. Covers employees, volunteers, retirees, and staff.
-- phone_number is the business identifier; id is the internal PK.

CREATE TABLE users (
    id              BIGSERIAL       PRIMARY KEY,
    full_name       VARCHAR(100)    NOT NULL,
    phone_number    VARCHAR(20)     NOT NULL UNIQUE,
    email           VARCHAR(150),
    address_text    TEXT,
    user_type       user_type       NOT NULL DEFAULT 'EMPLOYEE',

    -- Role/permission flags
    can_request_transport   BOOLEAN NOT NULL DEFAULT TRUE,
    can_drive               BOOLEAN NOT NULL DEFAULT FALSE,
    is_shift_manager        BOOLEAN NOT NULL DEFAULT FALSE,
    is_system_admin         BOOLEAN NOT NULL DEFAULT FALSE,
    is_active               BOOLEAN NOT NULL DEFAULT TRUE,

    created_at      TIMESTAMP       NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP       NOT NULL DEFAULT NOW()
);

-- Index for quick lookup by phone number (already UNIQUE, but explicit)
CREATE INDEX idx_users_phone ON users (phone_number);

-- ============================================================
-- TABLE: shift_types
-- ============================================================
-- Reference table for shift definitions.
-- Seeded via data.sql. Rarely changes at runtime.

CREATE TABLE shift_types (
    id          BIGSERIAL       PRIMARY KEY,
    name        shift_name      NOT NULL UNIQUE,
    start_time  TIME            NOT NULL,
    end_time    TIME            NOT NULL,
    is_active   BOOLEAN         NOT NULL DEFAULT TRUE
);

-- ============================================================
-- TABLE: transport_events
-- ============================================================
-- Represents a specific date + shift that requires transport coordination.
-- Created manually by manager (auto-creation planned for Phase 3).

CREATE TABLE transport_events (
    id                  BIGSERIAL               PRIMARY KEY,
    event_date          DATE                    NOT NULL,
    shift_type_id       BIGINT                  NOT NULL REFERENCES shift_types(id),
    transport_required  BOOLEAN                 NOT NULL DEFAULT TRUE,
    status              transport_event_status  NOT NULL DEFAULT 'OPEN',
    assigned_driver_id  BIGINT                  REFERENCES users(id),
    notes               TEXT,
    created_at          TIMESTAMP               NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP               NOT NULL DEFAULT NOW(),

    -- One event per date + shift combination
    CONSTRAINT uq_event_date_shift UNIQUE (event_date, shift_type_id)
);

CREATE INDEX idx_transport_events_date ON transport_events (event_date);
CREATE INDEX idx_transport_events_status ON transport_events (status);
CREATE INDEX idx_transport_events_driver ON transport_events (assigned_driver_id);

-- ============================================================
-- TABLE: transport_requests
-- ============================================================
-- An employee's request to be included in a specific transport event.

CREATE TABLE transport_requests (
    id                  BIGSERIAL           PRIMARY KEY,
    user_id             BIGINT              NOT NULL REFERENCES users(id),
    transport_event_id  BIGINT              NOT NULL REFERENCES transport_events(id),
    direction           request_direction   NOT NULL,
    status              request_status      NOT NULL DEFAULT 'PENDING',
    created_at          TIMESTAMP           NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP           NOT NULL DEFAULT NOW(),

    -- One request per user per event
    CONSTRAINT uq_user_event UNIQUE (user_id, transport_event_id)
);

CREATE INDEX idx_transport_requests_event ON transport_requests (transport_event_id);
CREATE INDEX idx_transport_requests_user ON transport_requests (user_id);
CREATE INDEX idx_transport_requests_status ON transport_requests (status);

-- ============================================================
-- TABLE: special_dates
-- ============================================================
-- Manually defined dates with special transport relevance.
-- Used for holidays, holiday eves, wartime periods, etc.

CREATE TABLE special_dates (
    id                  BIGSERIAL           PRIMARY KEY,
    special_date        DATE                NOT NULL UNIQUE,
    date_type           special_date_type   NOT NULL,
    description         VARCHAR(200),
    transport_relevant  BOOLEAN             NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMP           NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_special_dates_date ON special_dates (special_date);
