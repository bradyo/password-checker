CREATE TABLE info (
  id BIGSERIAL PRIMARY KEY,
  password_hash_count INT,
  import_started_at TIMESTAMP,
  import_completed_at TIMESTAMP
);

CREATE TABLE password_hash (
  id BIGSERIAL PRIMARY KEY,
  sha1_hash VARCHAR(40) NOT NULL,
  count INT
);
CREATE UNIQUE INDEX name ON password_hash (sha1_hash);
CREATE INDEX ON password_hash (count);
