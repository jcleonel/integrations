CREATE TABLE IF NOT EXISTS orders (
    id BIGSERIAL,
    customer_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL,
    total_price NUMERIC(10,2),
    order_date TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT pk_orders PRIMARY KEY (id),
    CONSTRAINT fk_orders_customers FOREIGN KEY (customer_id) REFERENCES customers(id)
);