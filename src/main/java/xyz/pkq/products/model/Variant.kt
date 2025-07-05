package xyz.pkq.products.model

import java.time.LocalDateTime

data class Variant(
    val id: Long,
    val productId: Long,
    val title: String,
    val price: String?,
    val available: Boolean?,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
)