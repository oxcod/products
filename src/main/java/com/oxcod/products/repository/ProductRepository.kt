package com.oxcod.products.repository

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Repository
import com.oxcod.products.model.Product
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import java.sql.PreparedStatement

@Repository
class ProductRepository(
    private val jdbcClient: JdbcClient,
    private val jdbcTemplate: JdbcTemplate,
    private val objectMapper: ObjectMapper
) {

    fun findAll(): List<Product> {
        return jdbcClient.sql("SELECT id, title, product_type, tags, created_at, updated_at FROM product")
            .query { rs, _ ->
                Product(
                    id = rs.getLong("id"),
                    title = rs.getString("title"),
                    productType = rs.getString("product_type"),
                    tags = parseTagsFromJson(rs.getString("tags")),
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
        return jdbcClient.sql("SELECT id, title, product_type, tags, created_at, updated_at FROM product ORDER BY id LIMIT :limit OFFSET :offset")
            .param("limit", limit)
            .param("offset", offset)
            .query { rs, _ ->
                Product(
                    id = rs.getLong("id"),
                    title = rs.getString("title"),
                    productType = rs.getString("product_type"),
                    tags = parseTagsFromJson(rs.getString("tags")),
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
        
        return jdbcClient.sql("SELECT id, title, product_type, tags, created_at, updated_at FROM product ORDER BY $column $order LIMIT :limit OFFSET :offset")
            .param("limit", limit)
            .param("offset", offset)
            .query { rs, _ ->
                Product(
                    id = rs.getLong("id"),
                    title = rs.getString("title"),
                    productType = rs.getString("product_type"),
                    tags = parseTagsFromJson(rs.getString("tags")),
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
        
        return jdbcClient.sql("SELECT id, title, product_type, tags, created_at, updated_at FROM product WHERE LOWER(title) LIKE LOWER(:query) ORDER BY $column $order")
            .param("query", "%$query%")
            .query { rs, _ ->
                Product(
                    id = rs.getLong("id"),
                    title = rs.getString("title"),
                    productType = rs.getString("product_type"),
                    tags = parseTagsFromJson(rs.getString("tags")),
                    createdAt = rs.getTimestamp("created_at")?.toLocalDateTime(),
                    updatedAt = rs.getTimestamp("updated_at")?.toLocalDateTime()
                )
            }
            .list()
    }
    
    fun findById(id: Long): Product? {
        return jdbcClient.sql("SELECT id, title, product_type, tags, created_at, updated_at FROM product WHERE id = :id")
            .param("id", id)
            .query { rs, _ ->
                Product(
                    id = rs.getLong("id"),
                    title = rs.getString("title"),
                    productType = rs.getString("product_type"),
                    tags = parseTagsFromJson(rs.getString("tags")),
                    createdAt = rs.getTimestamp("created_at")?.toLocalDateTime(),
                    updatedAt = rs.getTimestamp("updated_at")?.toLocalDateTime()
                )
            }
            .optional()
            .orElse(null)
    }

    fun searchByTitle(query: String): List<Product> {
        return jdbcClient.sql("SELECT id, title, product_type, tags, created_at, updated_at FROM product WHERE LOWER(title) LIKE LOWER(:query)")
            .param("query", "%$query%")
            .query { rs, _ ->
                Product(
                    id = rs.getLong("id"),
                    title = rs.getString("title"),
                    productType = rs.getString("product_type"),
                    tags = parseTagsFromJson(rs.getString("tags")),
                    createdAt = rs.getTimestamp("created_at")?.toLocalDateTime(),
                    updatedAt = rs.getTimestamp("updated_at")?.toLocalDateTime()
                )
            }
            .list()
    }
    
    fun save(product: Product): Product {
        jdbcClient.sql("""
            INSERT INTO product (id, title, product_type, tags) 
            VALUES (:id, :title, :productType, :tags::jsonb)
            ON CONFLICT (id) DO UPDATE 
            SET title = EXCLUDED.title, product_type = EXCLUDED.product_type, tags = EXCLUDED.tags
        """)
            .param("id", product.id)
            .param("title", product.title)
            .param("productType", product.productType)
            .param("tags", serializeTagsToJson(product.tags))
            .update()
        
        return product
    }

    fun saveAll(products: List<Product>) {
        if (products.isEmpty()) return
        
        val sql = """
            INSERT INTO product (id, title, product_type, tags) 
            VALUES (?, ?, ?, ?::jsonb)
            ON CONFLICT (id) DO UPDATE 
            SET title = EXCLUDED.title, product_type = EXCLUDED.product_type, tags = EXCLUDED.tags
        """
        
        jdbcTemplate.batchUpdate(sql, products, products.size) { ps: PreparedStatement, product: Product ->
            ps.setLong(1, product.id)
            ps.setString(2, product.title)
            ps.setString(3, product.productType)
            ps.setString(4, serializeTagsToJson(product.tags))
        }
    }
    
    fun deleteById(id: Long) {
        jdbcClient.sql("DELETE FROM product WHERE id = :id")
            .param("id", id)
            .update()
    }
    
    fun deleteAll() {
        jdbcClient.sql("DELETE FROM product").update()
    }
    
    private fun parseTagsFromJson(jsonString: String?): List<String>? {
        return if (jsonString.isNullOrBlank()) {
            null
        } else {
            try {
                objectMapper.readValue(jsonString, object : TypeReference<List<String>>() {})
            } catch (e: Exception) {
                null
            }
        }
    }
    
    private fun serializeTagsToJson(tags: List<String>?): String? {
        return if (tags.isNullOrEmpty()) {
            null
        } else {
            try {
                objectMapper.writeValueAsString(tags)
            } catch (e: Exception) {
                null
            }
        }
    }
}