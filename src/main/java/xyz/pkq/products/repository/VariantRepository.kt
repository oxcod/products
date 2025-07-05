package xyz.pkq.products.repository

import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Repository
import xyz.pkq.products.model.Variant

@Repository
class VariantRepository(private val jdbcClient: JdbcClient) {

    fun findByProductId(productId: Long): List<Variant> {
        return jdbcClient.sql("""
            SELECT id, product_id, title, price, available, created_at, updated_at 
            FROM variant 
            WHERE product_id = :productId
        """)
            .param("productId", productId)
            .query { rs, _ ->
                Variant(
                    id = rs.getLong("id"),
                    productId = rs.getLong("product_id"),
                    title = rs.getString("title"),
                    price = rs.getString("price"),
                    available = rs.getObject("available") as? Boolean,
                    createdAt = rs.getTimestamp("created_at")?.toLocalDateTime(),
                    updatedAt = rs.getTimestamp("updated_at")?.toLocalDateTime()
                )
            }
            .list()
    }

    fun findAll(): List<Variant> {
        return jdbcClient.sql("SELECT id, product_id, title, price, available, created_at, updated_at FROM variant")
            .query { rs, _ ->
                Variant(
                    id = rs.getLong("id"),
                    productId = rs.getLong("product_id"),
                    title = rs.getString("title"),
                    price = rs.getString("price"),
                    available = rs.getObject("available") as? Boolean,
                    createdAt = rs.getTimestamp("created_at")?.toLocalDateTime(),
                    updatedAt = rs.getTimestamp("updated_at")?.toLocalDateTime()
                )
            }
            .list()
    }

    fun save(variant: Variant): Variant {
        jdbcClient.sql("""
            INSERT INTO variant (id, product_id, title, price, available) 
            VALUES (:id, :productId, :title, :price, :available)
            ON CONFLICT (id) DO UPDATE 
            SET product_id = EXCLUDED.product_id, 
                title = EXCLUDED.title, 
                price = EXCLUDED.price, 
                available = EXCLUDED.available
        """)
            .param("id", variant.id)
            .param("productId", variant.productId)
            .param("title", variant.title)
            .param("price", variant.price)
            .param("available", variant.available)
            .update()
        
        return variant
    }

    fun deleteByProductId(productId: Long) {
        jdbcClient.sql("DELETE FROM variant WHERE product_id = :productId")
            .param("productId", productId)
            .update()
    }

    fun deleteAll() {
        jdbcClient.sql("DELETE FROM variant").update()
    }
}