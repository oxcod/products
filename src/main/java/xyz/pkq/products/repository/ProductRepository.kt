package xyz.pkq.products.repository

import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Repository
import xyz.pkq.products.model.Product

@Repository
class ProductRepository(private val jdbcClient: JdbcClient) {

    fun findAll(): List<Product> {
        return jdbcClient.sql("SELECT id, title, product_type, created_at, updated_at FROM product")
            .query { rs, _ ->
                Product(
                    id = rs.getLong("id"),
                    title = rs.getString("title"),
                    productType = rs.getString("product_type"),
                    createdAt = rs.getTimestamp("created_at")?.toLocalDateTime(),
                    updatedAt = rs.getTimestamp("updated_at")?.toLocalDateTime()
                )
            }
            .list()
    }

    fun findById(id: Long): Product? {
        return jdbcClient.sql("SELECT id, title, product_type, created_at, updated_at FROM product WHERE id = :id")
            .param("id", id)
            .query { rs, _ ->
                Product(
                    id = rs.getLong("id"),
                    title = rs.getString("title"),
                    productType = rs.getString("product_type"),
                    createdAt = rs.getTimestamp("created_at")?.toLocalDateTime(),
                    updatedAt = rs.getTimestamp("updated_at")?.toLocalDateTime()
                )
            }
            .optional()
            .orElse(null)
    }

    fun save(product: Product): Product {
        jdbcClient.sql("""
            INSERT INTO product (id, title, product_type) 
            VALUES (:id, :title, :productType)
            ON CONFLICT (id) DO UPDATE 
            SET title = EXCLUDED.title, product_type = EXCLUDED.product_type
        """)
            .param("id", product.id)
            .param("title", product.title)
            .param("productType", product.productType)
            .update()
        
        return product
    }

    fun deleteAll() {
        jdbcClient.sql("DELETE FROM product").update()
    }
}