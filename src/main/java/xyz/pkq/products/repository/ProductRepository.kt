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

    fun count(): Int {
        return jdbcClient.sql("SELECT COUNT(*) FROM product")
            .query(Int::class.java)
            .single()
    }
    
    fun findAllPaginated(offset: Int, limit: Int): List<Product> {
        return jdbcClient.sql("SELECT id, title, product_type, created_at, updated_at FROM product ORDER BY id LIMIT :limit OFFSET :offset")
            .param("limit", limit)
            .param("offset", offset)
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
    
    fun findAllPaginatedSorted(offset: Int, limit: Int, sortBy: String, sortOrder: String): List<Product> {
        val validSortColumns = setOf("id", "title")
        val column = if (sortBy in validSortColumns) sortBy else "id"
        val order = if (sortOrder.lowercase() == "desc") "DESC" else "ASC"
        
        return jdbcClient.sql("SELECT id, title, product_type, created_at, updated_at FROM product ORDER BY $column $order LIMIT :limit OFFSET :offset")
            .param("limit", limit)
            .param("offset", offset)
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
    
    fun searchByTitleSorted(query: String, sortBy: String, sortOrder: String): List<Product> {
        val validSortColumns = setOf("id", "title")
        val column = if (sortBy in validSortColumns) sortBy else "id"
        val order = if (sortOrder.lowercase() == "desc") "DESC" else "ASC"
        
        return jdbcClient.sql("SELECT id, title, product_type, created_at, updated_at FROM product WHERE LOWER(title) LIKE LOWER(:query) ORDER BY $column $order")
            .param("query", "%$query%")
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

    fun searchByTitle(query: String): List<Product> {
        return jdbcClient.sql("SELECT id, title, product_type, created_at, updated_at FROM product WHERE LOWER(title) LIKE LOWER(:query)")
            .param("query", "%$query%")
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

    fun deleteById(id: Long) {
        jdbcClient.sql("DELETE FROM product WHERE id = :id")
            .param("id", id)
            .update()
    }
    
    fun deleteAll() {
        jdbcClient.sql("DELETE FROM product").update()
    }
}