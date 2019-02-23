CREATE TABLE abstractor_order_ratings
(
   id bigserial,
   version bigint NOT NULL DEFAULT 0,
   created_at timestamptz NOT NULL,
   order_id bigint NOT NULL, 
   abstractor_id bigint NOT NULL, 
   rate integer NOT NULL, 
   CONSTRAINT pk_abstractor_order_ratings PRIMARY KEY (id),
   CONSTRAINT fk_abstractor_order_ratings_order_id FOREIGN KEY (order_id) REFERENCES orders (id) ON UPDATE NO ACTION ON DELETE NO ACTION,
   CONSTRAINT fk_abstractor_order_ratings_abstractor_id FOREIGN KEY (abstractor_id) REFERENCES users (id) ON UPDATE NO ACTION ON DELETE NO ACTION,
   CONSTRAINT u_absractor_order_ratings_order_id_abstractor_id UNIQUE (order_id, abstractor_id)
);
