CREATE TABLE abstractor_counties
(
   abstractor_id bigint NOT NULL, 
   county_id bigint NOT NULL, 
   CONSTRAINT pk_abstractor_counties PRIMARY KEY (abstractor_id, county_id),
   CONSTRAINT fk_abstractor_counties_abstractor_id FOREIGN KEY (abstractor_id) REFERENCES users (id) ON UPDATE NO ACTION ON DELETE NO ACTION,
   CONSTRAINT fk_abstractor_counties_county_id FOREIGN KEY (county_id) REFERENCES counties (id) ON UPDATE NO ACTION ON DELETE NO ACTION
);