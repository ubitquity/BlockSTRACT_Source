CREATE TABLE abstract_rates
(
   id bigserial, 
   version bigint NOT NULL DEFAULT 0, 
   created_at timestamptz NOT NULL, 
   user_id bigint NOT NULL, 
   service_type character varying(50) NOT NULL, 
   rate numeric(10,2), 
   CONSTRAINT pk_abstract_rates PRIMARY KEY (id), 
   CONSTRAINT fk_abstract_rates_user_id FOREIGN KEY (user_id) REFERENCES users (id) ON UPDATE NO ACTION ON DELETE NO ACTION
);
