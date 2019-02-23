CREATE TABLE abstractor_declined_orders
(
   abstractor_id bigint NOT NULL, 
   order_id bigint NOT NULL, 
   CONSTRAINT pk_abstractor_declined_orders PRIMARY KEY (abstractor_id, order_id),
   CONSTRAINT fk_abstractor_declined_orders_abstractor_id FOREIGN KEY (abstractor_id) REFERENCES users (id) ON UPDATE NO ACTION ON DELETE NO ACTION,
   CONSTRAINT fk_abstractor_declined_orders_order_id FOREIGN KEY (order_id) REFERENCES orders (id) ON UPDATE NO ACTION ON DELETE NO ACTION
);