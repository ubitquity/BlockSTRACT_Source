INSERT INTO users
(username, password, role, enabled, account_locked, first_name, last_name, email, deleted, created_at, account_activated)
VALUES
('system', '', 'SYSTEM', true, false, 'SYSTEM', UPPER('appstract'), '', false, '2000-01-01', true);

-- haslo: test
INSERT INTO users
(username, password, role, enabled, account_locked, first_name, last_name, email, deleted, created_at, account_activated)
VALUES
('root', '$2a$06$c5fFoy3SWmjbp3mjo/dQouqjpGtKIwM5izGAGX9R.rlI9yPcgPNZC', 'ROOT', true, false, 'Bartosz', 'Pieślak', 'bartosz.pieslak@itcraft.pl', false, '2000-01-01', true),
('admin', '$2a$06$c5fFoy3SWmjbp3mjo/dQouqjpGtKIwM5izGAGX9R.rlI9yPcgPNZC', 'ADMIN', true, false, 'Radek', 'Michalski', 'radoslaw.michalski@itcraft.pl', false, '2000-01-01', true);
