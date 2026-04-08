-- ============================================================
-- Shift Transport System — Initial Database Schema (MVP)
-- Version: 1.2  (idempotent — safe to run on every startup)
-- Enums stored as VARCHAR — standard JPA, no custom PG types needed
-- ============================================================

-- ============================================================
-- TABLE: users
-- ============================================================

CREATE TABLE IF NOT EXISTS users (
    id              BIGSERIAL       PRIMARY KEY,
    full_name       VARCHAR(100)    NOT NULL,
    phone_number    VARCHAR(20)     NOT NULL UNIQUE,
    email           VARCHAR(150),
    address_text    TEXT,
    user_type       VARCHAR(50)     NOT NULL DEFAULT 'EMPLOYEE',

    can_request_transport   BOOLEAN NOT NULL DEFAULT TRUE,
    can_drive               BOOLEAN NOT NULL DEFAULT FALSE,
    is_shift_manager        BOOLEAN NOT NULL DEFAULT FALSE,
    is_system_admin         BOOLEAN NOT NULL DEFAULT FALSE,
    is_active               BOOLEAN NOT NULL DEFAULT TRUE,

    created_at      TIMESTAMP       NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP       NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_users_phone ON users (phone_number);

-- ============================================================
-- TABLE: shift_types
-- ============================================================

CREATE TABLE IF NOT EXISTS shift_types (
    id          BIGSERIAL       PRIMARY KEY,
    name        VARCHAR(20)     NOT NULL UNIQUE,
    start_time  TIME            NOT NULL,
    end_time    TIME            NOT NULL,
    is_active   BOOLEAN         NOT NULL DEFAULT TRUE
);

-- ============================================================
-- TABLE: transport_events
-- ============================================================

CREATE TABLE IF NOT EXISTS transport_events (
    id                  BIGSERIAL       PRIMARY KEY,
    event_date          DATE            NOT NULL,
    shift_type_id       BIGINT          NOT NULL REFERENCES shift_types(id),
    transport_required  BOOLEAN         NOT NULL DEFAULT TRUE,
    status              VARCHAR(30)     NOT NULL DEFAULT 'OPEN',
    assigned_driver_id  BIGINT          REFERENCES users(id),
    notes               TEXT,
    created_at          TIMESTAMP       NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP       NOT NULL DEFAULT NOW(),

    CONSTRAINT uq_event_date_shift UNIQUE (event_date, shift_type_id)
);

CREATE INDEX IF NOT EXISTS idx_transport_events_date   ON transport_events (event_date);
CREATE INDEX IF NOT EXISTS idx_transport_events_status ON transport_events (status);
CREATE INDEX IF NOT EXISTS idx_transport_events_driver ON transport_events (assigned_driver_id);

-- ============================================================
-- TABLE: transport_requests
-- ============================================================

CREATE TABLE IF NOT EXISTS transport_requests (
    id                  BIGSERIAL   PRIMARY KEY,
    user_id             BIGINT      NOT NULL REFERENCES users(id),
    transport_event_id  BIGINT      NOT NULL REFERENCES transport_events(id),
    direction           VARCHAR(20) NOT NULL,
    status              VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    created_at          TIMESTAMP   NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP   NOT NULL DEFAULT NOW(),

    CONSTRAINT uq_user_event UNIQUE (user_id, transport_event_id)
);

CREATE INDEX IF NOT EXISTS idx_transport_requests_event  ON transport_requests (transport_event_id);
CREATE INDEX IF NOT EXISTS idx_transport_requests_user   ON transport_requests (user_id);
CREATE INDEX IF NOT EXISTS idx_transport_requests_status ON transport_requests (status);

-- ============================================================
-- TABLE: special_dates
-- ============================================================

CREATE TABLE IF NOT EXISTS special_dates (
    id                  BIGSERIAL       PRIMARY KEY,
    special_date        DATE            NOT NULL UNIQUE,
    date_type           VARCHAR(30)     NOT NULL,
    description         VARCHAR(200),
    transport_relevant  BOOLEAN         NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMP       NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_special_dates_date ON special_dates (special_date);
