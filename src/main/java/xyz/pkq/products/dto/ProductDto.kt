package xyz.pkq.products.dto

data class ProductWithVariants(
    val product: Product,
    val variants: List<Variant>
)

data class Product(
    val id: Long,
    val title: String,
    val productType: String?
)

data class Variant(
    val id: Long,
    val title: String,
    val price: String?,
    val available: Boolean?
)