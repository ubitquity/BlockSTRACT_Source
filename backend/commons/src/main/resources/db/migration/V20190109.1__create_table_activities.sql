CREATE TABLE activities
(
   id bigserial,
   version bigint NOT NULL DEFAULT 0,
   created_at timestamptz NOT NULL,
   name character varying(255) NOT NULL,
   details text NOT NULL,
   order_id bigint NOT NULL, 
   CONSTRAINT pk_activities PRIMARY KEY (id),
   CONSTRAINT fk_activities_order_id FOREIGN KEY (order_id) REFERENCES orders (id) ON UPDATE NO ACTION ON DELETE NO ACTION
);
