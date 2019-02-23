ALTER TABLE inbox_conversations ADD COLUMN admin_new_message boolean NOT NULL DEFAULT FALSE;
ALTER TABLE inbox_conversations ADD COLUMN abstractor_new_message boolean NOT NULL DEFAULT FALSE;