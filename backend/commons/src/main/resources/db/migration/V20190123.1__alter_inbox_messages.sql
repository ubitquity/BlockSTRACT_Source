ALTER TABLE inbox_messages ADD COLUMN assigned_abstractor_id bigint;

ALTER TABLE inbox_messages
  ADD CONSTRAINT fk_inbox_messages_assigned_abstractor_id FOREIGN KEY (assigned_abstractor_id) REFERENCES users (id) ON UPDATE NO ACTION ON DELETE NO ACTION;