CREATE TABLE service_types
(
   id bigserial, 
   version bigint NOT NULL DEFAULT 0, 
   created_at timestamptz NOT NULL, 
   name character varying(255) NOT NULL, 
   CONSTRAINT pk_service_types PRIMARY KEY (id)
);

INSERT INTO service_types(created_at, name) VALUES(NOW(), 'Document Retrieval');
INSERT INTO service_types(created_at, name) VALUES(NOW(), 'Current Owner');
INSERT INTO service_types(created_at, name) VALUES(NOW(), 'Two-Owner');
INSERT INTO service_types(created_at, name) VALUES(NOW(), 'Developer Search');
INSERT INTO service_types(created_at, name) VALUES(NOW(), 'Full Title Search');
INSERT INTO service_types(created_at, name) VALUES(NOW(), 'Asset Search');
INSERT INTO service_types(created_at, name) VALUES(NOW(), 'Update and Records');
INSERT INTO service_types(created_at, name) VALUES(NOW(), 'Probate Searches');
INSERT INTO service_types(created_at, name) VALUES(NOW(), 'Tax Information');

ALTER TABLE abstract_rates DROP COLUMN service_type;
ALTER TABLE abstract_rates ADD COLUMN service_type_id bigint NOT NULL;
ALTER TABLE abstract_rates ADD CONSTRAINT uq_abstract_rates_service_type_id_user_id UNIQUE (service_type_id, user_id);