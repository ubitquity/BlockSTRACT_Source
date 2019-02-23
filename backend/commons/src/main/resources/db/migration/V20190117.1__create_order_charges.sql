CREATE TABLE order_charges
(
   id bigserial, 
   version bigint NOT NULL DEFAULT 0, 
   created_at timestamptz NOT NULL, 
   order_abstract_rate_id bigint NOT NULL, 
   order_id bigint NOT NULL, 
   units integer NOT NULL,
   CONSTRAINT pk_order_charges PRIMARY KEY (id), 
   CONSTRAINT fk_order_charges_order_abstract_rate_id FOREIGN KEY (order_abstract_rate_id) REFERENCES order_abstract_rates (id) ON UPDATE NO ACTION ON DELETE NO ACTION,
   CONSTRAINT fk_order_charges_order_id FOREIGN KEY (order_id) REFERENCES orders (id) ON UPDATE NO ACTION ON DELETE NO ACTION
);