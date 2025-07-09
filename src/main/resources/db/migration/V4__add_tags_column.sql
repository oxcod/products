-- Add tags column to products table
ALTER TABLE product ADD COLUMN tags JSONB;

-- Add index for tags JSON queries (for future search functionality)
CREATE INDEX idx_products_tags ON product USING GIN (tags);

-- Add comment for documentation
COMMENT ON COLUMN product.tags IS 'Product tags stored as JSON array of strings';