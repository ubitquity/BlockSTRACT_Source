CREATE TABLE inbox_conversations
(
   id bigserial, 
   version bigint NOT NULL DEFAULT 0, 
   created_at timestamptz NOT NULL, 
   order_id bigint NOT NULL, 
   last_message_id bigint,
   CONSTRAINT pk_inbox_conversations PRIMARY KEY (id),
   CONSTRAINT fk_inbox_conversations_order_id FOREIGN KEY (order_id) REFERENCES orders (id) ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE inbox_messages
(
   id bigserial, 
   version bigint NOT NULL DEFAULT 0, 
   created_at timestamptz NOT NULL, 
   inbox_conversation_id bigint NOT NULL, 
   sender_id bigint NOT NULL, 
   content text,
   CONSTRAINT pk_inbox_messages PRIMARY KEY (id),
   CONSTRAINT fk_inbox_messages_inbox_conversation_id FOREIGN KEY (inbox_conversation_id) REFERENCES inbox_conversations (id) ON UPDATE NO ACTION ON DELETE NO ACTION,
   CONSTRAINT fk_inbox_messages_inbox_sender_id FOREIGN KEY (sender_id) REFERENCES users (id) ON UPDATE NO ACTION ON DELETE NO ACTION
);

ALTER TABLE inbox_conversations
  ADD CONSTRAINT fk_inbox_conversations_last_message_id FOREIGN KEY (last_message_id) REFERENCES inbox_messages (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

CREATE TABLE inbox_message_attachments
(
   id bigserial, 
   version bigint NOT NULL DEFAULT 0, 
   created_at timestamptz NOT NULL, 
   inbox_message_id bigint NOT NULL, 
   uploaded_file_id bigint NOT NULL, 
   CONSTRAINT pk_inbox_message_attachments PRIMARY KEY (id),
   CONSTRAINT fk_inbox_message_attachments_inbox_message_id FOREIGN KEY (inbox_message_id) REFERENCES inbox_messages (id) ON UPDATE NO ACTION ON DELETE NO ACTION,
   CONSTRAINT fk_inbox_message_attachments_uploaded_file_id FOREIGN KEY (uploaded_file_id) REFERENCES uploaded_files (id) ON UPDATE NO ACTION ON DELETE NO ACTION
);

ALTER TABLE orders
  ADD COLUMN abstractor_id bigint;
ALTER TABLE orders
  ADD CONSTRAINT fk_orders_abstractor_id FOREIGN KEY (abstractor_id) REFERENCES users (id) ON UPDATE NO ACTION ON DELETE NO ACTION;
  