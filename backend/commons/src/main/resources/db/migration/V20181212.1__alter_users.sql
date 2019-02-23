ALTER TABLE users DROP COLUMN username;
ALTER TABLE users ADD CONSTRAINT uq_users_email UNIQUE (email);