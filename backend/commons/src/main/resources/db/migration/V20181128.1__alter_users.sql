ALTER TABLE users
  ADD COLUMN bank_account character varying(255);
ALTER TABLE users
  ADD COLUMN notifications boolean NOT NULL DEFAULT true;
