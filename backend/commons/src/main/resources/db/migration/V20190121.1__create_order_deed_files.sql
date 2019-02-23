CREATE TABLE order_deed_files
(
   id bigserial, 
   version bigint NOT NULL DEFAULT 0, 
   created_at timestamptz NOT NULL, 
   order_id bigint NOT NULL, 
   uploaded_file_id bigint NOT NULL, 
   CONSTRAINT pk_order_deed_files PRIMARY KEY (id),
   CONSTRAINT fk_order_deed_files_order_id FOREIGN KEY (order_id) REFERENCES orders (id) ON UPDATE NO ACTION ON DELETE NO ACTION,
   CONSTRAINT fk_order_deed_files_uploaded_file_id FOREIGN KEY (uploaded_file_id) REFERENCES uploaded_files (id) ON UPDATE NO ACTION ON DELETE NO ACTION
);

ALTER TABLE orders DROP COLUMN deed_file_id;