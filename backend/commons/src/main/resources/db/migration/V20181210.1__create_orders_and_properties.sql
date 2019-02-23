CREATE TABLE orders
(
   id bigserial, 
   version bigint NOT NULL DEFAULT 0, 
   created_at timestamptz NOT NULL, 
   qualia_id character varying(255) UNIQUE NOT NULL,
   order_number character varying(255) NOT NULL,
   status character varying(50) NOT NULL, 
   customer_name character varying(255) NOT NULL,
   product_name character varying(255) NOT NULL,
   price numeric(10,2), 
   due_date timestamptz NOT NULL, 
   open boolean NOT NULL,
   completed boolean NOT NULL,
   cancelled boolean NOT NULL,
   CONSTRAINT pk_orders PRIMARY KEY (id)
);

CREATE TABLE properties
(
   id bigserial, 
   version bigint NOT NULL DEFAULT 0, 
   created_at timestamptz NOT NULL, 
   flat_address character varying(255) NOT NULL,
   order_id bigint NOT NULL, 
   CONSTRAINT pk_properties PRIMARY KEY (id),
   CONSTRAINT fk_properties_order_id FOREIGN KEY (order_id) REFERENCES orders (id) ON UPDATE NO ACTION ON DELETE NO ACTION
);
