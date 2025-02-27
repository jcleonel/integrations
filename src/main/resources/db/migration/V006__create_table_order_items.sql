CREATE TABLE IF NOT EXISTS order_items (
    id BIGSERIAL,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    price NUMERIC(10,2) NOT NULL,
    CONSTRAINT pk_order_items PRIMARY KEY (id),
    CONSTRAINT fk_order_items_orders FOREIGN KEY (order_id) REFERENCES orders(id),
    CONSTRAINT fk_order_items_products FOREIGN KEY (product_id) REFERENCES products(id)
);