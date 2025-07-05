-- Create indexes for better sorting performance
CREATE INDEX IF NOT EXISTS idx_product_id ON product(id);
CREATE INDEX IF NOT EXISTS idx_product_title ON product(title);
CREATE INDEX IF NOT EXISTS idx_product_title_lower ON product(LOWER(title));