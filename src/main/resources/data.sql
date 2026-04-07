-- ============================================================
-- Shift Transport System — Seed Data (MVP)
-- ============================================================

-- Shift types (reference data)
INSERT INTO shift_types (name, start_time, end_time, is_active) VALUES
    ('MORNING',   '07:00', '15:00', TRUE),
    ('AFTERNOON', '15:00', '21:00', TRUE),
    ('NIGHT',     '21:00', '07:00', TRUE);

-- Sample users for development/testing

-- Employee who can request transport
INSERT INTO users (full_name, phone_number, email, address_text, user_type, can_request_transport, can_drive, is_shift_manager, is_system_admin)
VALUES ('Israel Israeli', '0501234567', NULL, 'Herzl 10, Tel Aviv', 'EMPLOYEE', TRUE, FALSE, FALSE, FALSE);

-- Employee who can also drive
INSERT INTO users (full_name, phone_number, email, address_text, user_type, can_request_transport, can_drive, is_shift_manager, is_system_admin)
VALUES ('David Cohen', '0527654321', NULL, 'Ben Yehuda 5, Haifa', 'EMPLOYEE', TRUE, TRUE, FALSE, FALSE);

-- Volunteer driver (not an employee — cannot request transport)
INSERT INTO users (full_name, phone_number, email, address_text, user_type, can_request_transport, can_drive, is_shift_manager, is_system_admin)
VALUES ('Moshe Levi', '0539876543', NULL, 'Jabotinsky 20, Ramat Gan', 'VOLUNTEER', FALSE, TRUE, FALSE, FALSE);

-- Shift manager
INSERT INTO users (full_name, phone_number, email, address_text, user_type, can_request_transport, can_drive, is_shift_manager, is_system_admin)
VALUES ('Sarah Manager', '0541112233', 'sarah@example.com', 'Rothschild 15, Tel Aviv', 'EMPLOYEE', TRUE, FALSE, TRUE, FALSE);

-- System admin
INSERT INTO users (full_name, phone_number, email, address_text, user_type, can_request_transport, can_drive, is_shift_manager, is_system_admin)
VALUES ('Admin User', '0500000000', 'admin@example.com', NULL, 'STAFF', FALSE, FALSE, FALSE, TRUE);
