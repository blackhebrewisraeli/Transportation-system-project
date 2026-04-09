-- ============================================================
-- Shift Transport System — Seed Data (MVP)
-- Version: 1.1  (idempotent — safe to run on every startup)
-- ON CONFLICT DO NOTHING ensures re-runs skip existing rows.
-- ============================================================

-- Shift types (reference data)
INSERT INTO shift_types (name, start_time, end_time, is_active) VALUES
    ('MORNING',   '07:00', '15:00', TRUE),
    ('AFTERNOON', '15:00', '21:00', TRUE),
    ('NIGHT',     '21:00', '07:00', TRUE)
ON CONFLICT (name) DO NOTHING;

-- ============================================================
-- Default dev password for ALL seed users: Test@12345
-- BCrypt hash (cost=10, $2a$ prefix — Spring BCryptPasswordEncoder compatible).
-- Change these hashes before any real deployment.
-- ============================================================

-- Employee who can request transport
INSERT INTO users (full_name, phone_number, email, address_text, user_type, can_request_transport, can_drive, is_shift_manager, is_system_admin, password_hash)
VALUES ('Israel Israeli', '0501234567', NULL, 'Herzl 10, Tel Aviv', 'EMPLOYEE', TRUE, FALSE, FALSE, FALSE,
        '$2a$10$0.JwaPRXxTQpYdawxcswo.43n6LM4lpKoyn7p1dmkNAjtH9XniCjm')
ON CONFLICT (phone_number) DO UPDATE SET
    password_hash = CASE WHEN users.password_hash IS NULL THEN EXCLUDED.password_hash ELSE users.password_hash END;

-- Employee who can also drive
INSERT INTO users (full_name, phone_number, email, address_text, user_type, can_request_transport, can_drive, is_shift_manager, is_system_admin, password_hash)
VALUES ('David Cohen', '0527654321', NULL, 'Ben Yehuda 5, Haifa', 'EMPLOYEE', TRUE, TRUE, FALSE, FALSE,
        '$2a$10$0.JwaPRXxTQpYdawxcswo.43n6LM4lpKoyn7p1dmkNAjtH9XniCjm')
ON CONFLICT (phone_number) DO UPDATE SET
    password_hash = CASE WHEN users.password_hash IS NULL THEN EXCLUDED.password_hash ELSE users.password_hash END;

-- Volunteer driver (not an employee — cannot request transport)
INSERT INTO users (full_name, phone_number, email, address_text, user_type, can_request_transport, can_drive, is_shift_manager, is_system_admin, password_hash)
VALUES ('Moshe Levi', '0539876543', NULL, 'Jabotinsky 20, Ramat Gan', 'VOLUNTEER', FALSE, TRUE, FALSE, FALSE,
        '$2a$10$0.JwaPRXxTQpYdawxcswo.43n6LM4lpKoyn7p1dmkNAjtH9XniCjm')
ON CONFLICT (phone_number) DO UPDATE SET
    password_hash = CASE WHEN users.password_hash IS NULL THEN EXCLUDED.password_hash ELSE users.password_hash END;

-- Shift manager
INSERT INTO users (full_name, phone_number, email, address_text, user_type, can_request_transport, can_drive, is_shift_manager, is_system_admin, password_hash)
VALUES ('Sarah Manager', '0541112233', 'sarah@example.com', 'Rothschild 15, Tel Aviv', 'EMPLOYEE', TRUE, FALSE, TRUE, FALSE,
        '$2a$10$0.JwaPRXxTQpYdawxcswo.43n6LM4lpKoyn7p1dmkNAjtH9XniCjm')
ON CONFLICT (phone_number) DO UPDATE SET
    password_hash = CASE WHEN users.password_hash IS NULL THEN EXCLUDED.password_hash ELSE users.password_hash END;

-- System admin
INSERT INTO users (full_name, phone_number, email, address_text, user_type, can_request_transport, can_drive, is_shift_manager, is_system_admin, password_hash)
VALUES ('Admin User', '0500000000', 'admin@example.com', 'Admin Office', 'STAFF', FALSE, FALSE, FALSE, TRUE,
        '$2a$10$0.JwaPRXxTQpYdawxcswo.43n6LM4lpKoyn7p1dmkNAjtH9XniCjm')
ON CONFLICT (phone_number) DO UPDATE SET
    password_hash = CASE WHEN users.password_hash IS NULL THEN EXCLUDED.password_hash ELSE users.password_hash END;

-- ============================================================
-- Additional test employees (for realistic scenario testing)
-- ============================================================

INSERT INTO users (full_name, phone_number, email, address_text, user_type, can_request_transport, can_drive, is_shift_manager, is_system_admin, password_hash)
VALUES ('Yael Ben-David', '0521111111', NULL, 'Weizmann 3, Rishon LeZion', 'EMPLOYEE', TRUE, FALSE, FALSE, FALSE,
        '$2a$10$0.JwaPRXxTQpYdawxcswo.43n6LM4lpKoyn7p1dmkNAjtH9XniCjm')
ON CONFLICT (phone_number) DO UPDATE SET
    password_hash = CASE WHEN users.password_hash IS NULL THEN EXCLUDED.password_hash ELSE users.password_hash END;

INSERT INTO users (full_name, phone_number, email, address_text, user_type, can_request_transport, can_drive, is_shift_manager, is_system_admin, password_hash)
VALUES ('Avi Mizrahi', '0532222222', NULL, 'HaHistadrut 18, Holon', 'EMPLOYEE', TRUE, FALSE, FALSE, FALSE,
        '$2a$10$0.JwaPRXxTQpYdawxcswo.43n6LM4lpKoyn7p1dmkNAjtH9XniCjm')
ON CONFLICT (phone_number) DO UPDATE SET
    password_hash = CASE WHEN users.password_hash IS NULL THEN EXCLUDED.password_hash ELSE users.password_hash END;

INSERT INTO users (full_name, phone_number, email, address_text, user_type, can_request_transport, can_drive, is_shift_manager, is_system_admin, password_hash)
VALUES ('Noa Peretz', '0543333333', 'noa.peretz@example.com', 'Begin 44, Bat Yam', 'EMPLOYEE', TRUE, FALSE, FALSE, FALSE,
        '$2a$10$0.JwaPRXxTQpYdawxcswo.43n6LM4lpKoyn7p1dmkNAjtH9XniCjm')
ON CONFLICT (phone_number) DO UPDATE SET
    password_hash = CASE WHEN users.password_hash IS NULL THEN EXCLUDED.password_hash ELSE users.password_hash END;

INSERT INTO users (full_name, phone_number, email, address_text, user_type, can_request_transport, can_drive, is_shift_manager, is_system_admin, password_hash)
VALUES ('Roni Shapiro', '0554444444', NULL, 'HaNassi 7, Ramat HaSharon', 'EMPLOYEE', TRUE, FALSE, FALSE, FALSE,
        '$2a$10$0.JwaPRXxTQpYdawxcswo.43n6LM4lpKoyn7p1dmkNAjtH9XniCjm')
ON CONFLICT (phone_number) DO UPDATE SET
    password_hash = CASE WHEN users.password_hash IS NULL THEN EXCLUDED.password_hash ELSE users.password_hash END;

INSERT INTO users (full_name, phone_number, email, address_text, user_type, can_request_transport, can_drive, is_shift_manager, is_system_admin, password_hash)
VALUES ('Hila Katz', '0565555555', NULL, 'Jabotinsky 31, Petah Tikva', 'EMPLOYEE', TRUE, FALSE, FALSE, FALSE,
        '$2a$10$0.JwaPRXxTQpYdawxcswo.43n6LM4lpKoyn7p1dmkNAjtH9XniCjm')
ON CONFLICT (phone_number) DO UPDATE SET
    password_hash = CASE WHEN users.password_hash IS NULL THEN EXCLUDED.password_hash ELSE users.password_hash END;

INSERT INTO users (full_name, phone_number, email, address_text, user_type, can_request_transport, can_drive, is_shift_manager, is_system_admin, password_hash)
VALUES ('Danny Levi', '0576666666', 'danny.levi@example.com', 'Herzl 55, Rehovot', 'EMPLOYEE', TRUE, FALSE, FALSE, FALSE,
        '$2a$10$0.JwaPRXxTQpYdawxcswo.43n6LM4lpKoyn7p1dmkNAjtH9XniCjm')
ON CONFLICT (phone_number) DO UPDATE SET
    password_hash = CASE WHEN users.password_hash IS NULL THEN EXCLUDED.password_hash ELSE users.password_hash END;

INSERT INTO users (full_name, phone_number, email, address_text, user_type, can_request_transport, can_drive, is_shift_manager, is_system_admin, password_hash)
VALUES ('Michal Friedman', '0587777777', NULL, 'HaAtzmaut 12, Bnei Brak', 'EMPLOYEE', TRUE, FALSE, FALSE, FALSE,
        '$2a$10$0.JwaPRXxTQpYdawxcswo.43n6LM4lpKoyn7p1dmkNAjtH9XniCjm')
ON CONFLICT (phone_number) DO UPDATE SET
    password_hash = CASE WHEN users.password_hash IS NULL THEN EXCLUDED.password_hash ELSE users.password_hash END;

-- Employee without an address yet (tests the "missing address" case in the passenger list)
INSERT INTO users (full_name, phone_number, email, address_text, user_type, can_request_transport, can_drive, is_shift_manager, is_system_admin, password_hash)
VALUES ('Tal Golan', '0598888888', NULL, NULL, 'EMPLOYEE', TRUE, FALSE, FALSE, FALSE,
        '$2a$10$0.JwaPRXxTQpYdawxcswo.43n6LM4lpKoyn7p1dmkNAjtH9XniCjm')
ON CONFLICT (phone_number) DO UPDATE SET
    password_hash = CASE WHEN users.password_hash IS NULL THEN EXCLUDED.password_hash ELSE users.password_hash END;

-- ============================================================
-- Default address for users without an address
-- "Derech Agudat Beitar, Malha, Jerusalem" (Teddy Stadium)
-- Run this after the inserts above to fill in any NULL addresses.
-- ============================================================
UPDATE users
SET address_text = 'Derech Agudat Beitar, Malha, Jerusalem'
WHERE address_text IS NULL OR TRIM(address_text) = '';
