ALTER TABLE orders ADD COLUMN purpose character varying(255);
ALTER TABLE orders ADD COLUMN customer_contact character varying(255);
ALTER TABLE orders ADD COLUMN customer_order_number character varying(255);
ALTER TABLE orders ADD COLUMN product_description text;

CREATE TABLE sellers
(
   id bigserial, 
   version bigint NOT NULL DEFAULT 0, 
   created_at timestamptz NOT NULL, 
   name character varying(255) NOT NULL,
   order_id bigint NOT NULL, 
   CONSTRAINT pk_sellers PRIMARY KEY (id),
   CONSTRAINT fk_sellers_order_id FOREIGN KEY (order_id) REFERENCES orders (id) ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE borrowers
(
   id bigserial, 
   version bigint NOT NULL DEFAULT 0, 
   created_at timestamptz NOT NULL, 
   name character varying(255) NOT NULL,
   order_id bigint NOT NULL, 
   CONSTRAINT pk_borrowers PRIMARY KEY (id),
   CONSTRAINT fk_borrowers_order_id FOREIGN KEY (order_id) REFERENCES orders (id) ON UPDATE NO ACTION ON DELETE NO ACTION
);