ALTER TABLE activities DROP COLUMN details;
ALTER TABLE activities ADD COLUMN type character varying(255) NOT NULL DEFAULT 'MESSAGE';
ALTER TABLE activities ADD COLUMN abstractor_id bigint;
ALTER TABLE activities ADD COLUMN entity_id bigint;

ALTER TABLE activities
  ADD CONSTRAINT fk_activities_abstractor_id FOREIGN KEY (abstractor_id) REFERENCES users (id) ON UPDATE NO ACTION ON DELETE NO ACTION;