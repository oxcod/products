CREATE TABLE product (
                         id BIGINT PRIMARY KEY,
                         title VARCHAR(256) NOT NULL,
                         product_type VARCHAR(128)
);
CREATE TABLE variant (
                         id BIGINT PRIMARY KEY,
                         product_id BIGINT REFERENCES product(id) ON DELETE CASCADE,
                         title VARCHAR(128) NOT NULL,
                         price VARCHAR(32),
                         available BOOLEAN
);