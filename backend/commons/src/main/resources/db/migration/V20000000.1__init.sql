-- -----------------------------------
-- haversine_distance
-- -----------------------------------

CREATE OR REPLACE FUNCTION haversine_distance(lat1 double precision, lon1 double precision, lat2 double precision, lon2 double precision)
  RETURNS double precision AS
$BODY$
  SELECT
    ACOS( COS(RADIANS(lat1)) * COS(RADIANS(lat2)) * COS( RADIANS(lon2) - RADIANS(lon1) ) + SIN(RADIANS(lat1)) * SIN(RADIANS(lat2)) ) * 6371
$BODY$
  LANGUAGE sql IMMUTABLE STRICT
  COST 100;

-- -----------------------------------
-- users
-- -----------------------------------

CREATE TABLE users
(
  id  BIGSERIAL NOT NULL,
  version  BIGINT NOT NULL DEFAULT 0,
  created_at  TIMESTAMPTZ NOT NULL,
  username  VARCHAR(200) NOT NULL,
  role  VARCHAR(64) NOT NULL,
  email  VARCHAR(200) DEFAULT NULL,
  first_name  VARCHAR(255) DEFAULT NULL,
  last_name  VARCHAR(255) DEFAULT NULL,
  last_login_at TIMESTAMPTZ DEFAULT NULL,
  activation_token  VARCHAR(50) DEFAULT NULL,
  comment  TEXT DEFAULT NULL,
  avatar_id  BIGINT DEFAULT NULL,

  password  VARCHAR(64) NOT NULL,
  enabled  BOOLEAN NOT NULL DEFAULT true,
  deleted  BOOLEAN NOT NULL DEFAULT false,
  account_locked  BOOLEAN NOT NULL DEFAULT false,
  password_expiration_date  TIMESTAMPTZ DEFAULT NULL,
  incorrect_logins_count  INTEGER NOT NULL DEFAULT 0,
  needs_password_reset  BOOLEAN NOT NULL DEFAULT false,
  account_activated  BOOLEAN NOT NULL DEFAULT true,
  
  CONSTRAINT pk_users PRIMARY KEY (id),
  CONSTRAINT uq_users_username UNIQUE (username)
)
WITH (OIDS=FALSE);

-- -----------------------------------
-- uploaded_files
-- -----------------------------------

CREATE TABLE uploaded_files
(
  id  BIGSERIAL NOT NULL,
  version  BIGINT NOT NULL,
  created_at  TIMESTAMPTZ NOT NULL,
  
  file_path  VARCHAR(255) NOT NULL,
  temporary_file  BOOLEAN NOT NULL DEFAULT false,
  mime_type  VARCHAR(100) NOT NULL,
  source_file_name  VARCHAR(200) NOT NULL,
    
  CONSTRAINT pk_uploaded_files PRIMARY KEY (id)
)
WITH (OIDS=FALSE);


-- -----------------------------------
-- api_login_entries
-- -----------------------------------

CREATE TABLE api_login_entries
(
  id  BIGSERIAL NOT NULL,
  version  BIGINT NOT NULL DEFAULT 0,
  created_at  TIMESTAMPTZ NOT NULL,
  
  logout_time  TIMESTAMPTZ DEFAULT NULL,
  user_id  BIGINT NOT NULL,
  access_token  VARCHAR(100) NOT NULL,
  token_expiration_date  TIMESTAMPTZ NOT NULL,
  registration_id  VARCHAR(250) DEFAULT NULL,
  remote_host  VARCHAR(100) DEFAULT NULL,
  user_agent  VARCHAR(200) DEFAULT NULL,
  
  CONSTRAINT pk_api_login_entries PRIMARY KEY (id)
)
WITH (OIDS=FALSE);

-- -----------------------------------
-- user_archive_passwords
-- -----------------------------------

CREATE TABLE user_archive_passwords
(
  id  BIGSERIAL NOT NULL,
  version  BIGINT NOT NULL DEFAULT 0,
  created_at  TIMESTAMPTZ NOT NULL,
  
  user_id  BIGINT NOT NULL,
  password  VARCHAR(100) NOT NULL,
  
  CONSTRAINT pk_user_archive_passwords PRIMARY KEY (id)
)
WITH (OIDS=FALSE);

-- -----------------------------------
-- password_resets
-- -----------------------------------

CREATE TABLE password_resets
(
  id  BIGSERIAL NOT NULL,
  version  BIGINT NOT NULL DEFAULT 0,
  created_at  TIMESTAMPTZ NOT NULL,
  
  user_id  BIGINT NOT NULL,
  token  VARCHAR(64) NOT NULL,
  
  CONSTRAINT pk_password_resets PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);

-- -----------------------------------
-- FKs
-- -----------------------------------

ALTER TABLE users ADD CONSTRAINT fk_users_uploaded_files
  FOREIGN KEY (avatar_id) REFERENCES uploaded_files (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE api_login_entries ADD CONSTRAINT fk_api_login_entries_users
  FOREIGN KEY (user_id) REFERENCES users (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE user_archive_passwords ADD CONSTRAINT fk_user_archive_passwords_users
  FOREIGN KEY (user_id) REFERENCES users (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE password_resets ADD CONSTRAINT fk_pasword_resets_user_id
  FOREIGN KEY (user_id) REFERENCES users (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;

