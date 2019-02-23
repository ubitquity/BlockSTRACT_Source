CREATE TABLE counties
(
   id bigserial,
   version bigint NOT NULL DEFAULT 0,
   created_at timestamptz NOT NULL,
   name character varying(255) UNIQUE NOT NULL,
   CONSTRAINT pk_counties PRIMARY KEY (id)
);

INSERT INTO counties(created_at, name) VALUES(NOW(), 'Fulton');
INSERT INTO counties(created_at, name) VALUES(NOW(), 'Clarke');
INSERT INTO counties(created_at, name) VALUES(NOW(), 'Chatham');
INSERT INTO counties(created_at, name) VALUES(NOW(), 'Richmond');

ALTER TABLE properties DROP COLUMN county;
ALTER TABLE properties ADD COLUMN county_id bigint NOT NULL DEFAULT 1;
ALTER TABLE properties ADD CONSTRAINT fk_properties_county_id FOREIGN KEY (county_id) REFERENCES counties (id) ON UPDATE NO ACTION ON DELETE NO ACTION;
