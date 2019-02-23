CREATE TABLE notifications
(
   id bigserial,
   version bigint NOT NULL DEFAULT 0,
   created_at timestamptz NOT NULL,
   content text NOT NULL,
   type character varying(255) NOT NULL, 
   user_id bigint NOT NULL, 
   order_id bigint, 
   seen boolean NOT NULL, 
   CONSTRAINT pk_notifications PRIMARY KEY (id),
   CONSTRAINT fk_notifications_user_id FOREIGN KEY (user_id) REFERENCES users (id) ON UPDATE NO ACTION ON DELETE NO ACTION,
   CONSTRAINT fk_notifications_order_id FOREIGN KEY (order_id) REFERENCES orders (id) ON UPDATE NO ACTION ON DELETE NO ACTION
);
