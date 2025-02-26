CREATE TABLE IF NOT EXISTS products (
    id BIGSERIAL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL,
    CONSTRAINT pk_products PRIMARY KEY (id)
)