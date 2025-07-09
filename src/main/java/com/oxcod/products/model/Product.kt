package com.oxcod.products.model

import java.time.LocalDateTime

data class Product(
    val id: Long,
    val title: String,
    val productType: String?,
    val tags: List<String>? = null,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
)