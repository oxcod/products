package xyz.pkq.products.repository

import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Repository
import xyz.pkq.products.model.Variant

@Repository
class VariantRepository(private val jdbcClient: JdbcClient) {

    fun findByProductId(productId: Long): List<Variant> {
        return jdbcClient.sql("""
            SELECT id, product_id, title, price, available 
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
                    available = rs.getObject("available") as? Boolean
                )
            }
            .list()
    }

    fun findAll(): List<Variant> {
        return jdbcClient.sql("SELECT id, product_id, title, price, available FROM variant")
            .query { rs, _ ->
                Variant(
                    id = rs.getLong("id"),
                    productId = rs.getLong("product_id"),
                    title = rs.getString("title"),
                    price = rs.getString("price"),
                    available = rs.getObject("available") as? Boolean
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