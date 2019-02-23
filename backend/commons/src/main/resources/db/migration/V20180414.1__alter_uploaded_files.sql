ALTER TABLE uploaded_files ADD COLUMN secured boolean NOT NULL DEFAULT true;
ALTER TABLE uploaded_files ADD COLUMN to_delete boolean NOT NULL DEFAULT false;
