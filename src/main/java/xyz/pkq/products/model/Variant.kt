package xyz.pkq.products.model

data class Variant(
    val id: Long,
    val productId: Long,
    val title: String,
    val price: String?,
    val available: Boolean?
)