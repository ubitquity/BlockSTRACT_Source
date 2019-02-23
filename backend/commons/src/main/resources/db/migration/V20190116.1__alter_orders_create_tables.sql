ALTER TABLE orders ADD COLUMN start_date timestamptz;
ALTER TABLE orders ADD COLUMN end_date timestamptz;
ALTER TABLE orders ADD COLUMN title_vesting text;
ALTER TABLE orders ADD COLUMN legal_description text;
ALTER TABLE orders ADD COLUMN estate_type text;
ALTER TABLE orders ADD COLUMN outstanding_mortgage text;
ALTER TABLE orders ADD COLUMN commitment_requirements text;
ALTER TABLE orders ADD COLUMN commitment_exceptions text;
ALTER TABLE orders ADD COLUMN copy_cost_per_unit numeric(10,2);
ALTER TABLE orders ADD COLUMN copy_units integer;
ALTER TABLE orders ADD COLUMN deed_file_id bigint;
ALTER TABLE orders ADD COLUMN title_search_document_file_id bigint;

ALTER TABLE orders
  ADD CONSTRAINT fk_orders_deed_file_id FOREIGN KEY (deed_file_id) REFERENCES uploaded_files (id) ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE orders
  ADD CONSTRAINT fk_orders_title_search_document_file_id FOREIGN KEY (title_search_document_file_id) REFERENCES uploaded_files (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

CREATE TABLE order_abstract_rates
(
   id bigserial, 
   version bigint NOT NULL DEFAULT 0, 
   created_at timestamptz NOT NULL, 
   order_id bigint NOT NULL, 
   service_type_id bigint NOT NULL,
   rate numeric(10,2), 
   CONSTRAINT pk_order_abstract_rates PRIMARY KEY (id), 
   CONSTRAINT fk_order_abstract_rates_order_id FOREIGN KEY (order_id) REFERENCES orders (id) ON UPDATE NO ACTION ON DELETE NO ACTION,
   CONSTRAINT fk_order_abstract_rates_service_type_id FOREIGN KEY (service_type_id) REFERENCES service_types (id) ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE order_fulfillment_parcel_ids
(
   id bigserial, 
   version bigint NOT NULL DEFAULT 0, 
   created_at timestamptz NOT NULL, 
   parcel_id text, 
   order_id bigint NOT NULL,
   CONSTRAINT pk_order_fulfillment_parcel_ids PRIMARY KEY (id),
   CONSTRAINT fk_order_fulfillment_parcel_ids_order_id FOREIGN KEY (order_id) REFERENCES orders (id) ON UPDATE NO ACTION ON DELETE NO ACTION
);

ALTER TABLE abstract_rates
  ADD CONSTRAINT fk_abstract_rates_service_type_id FOREIGN KEY (service_type_id) REFERENCES service_types (id) ON UPDATE NO ACTION ON DELETE NO ACTION;
