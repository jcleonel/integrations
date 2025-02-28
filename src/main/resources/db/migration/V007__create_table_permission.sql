
CREATE TABLE IF NOT EXISTS permission (
    id BIGSERIAL,
    description VARCHAR(255) DEFAULT NULL,
    CONSTRAINT pk_permission PRIMARY KEY (id)
);