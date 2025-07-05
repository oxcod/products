package xyz.pkq.products.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class FammeProduct(
    val id: Long,
    val title: String,
    @JsonProperty("product_type")
    val productType: String?,
    val variants: List<FammeVariant>
)

data class FammeVariant(
    val id: Long,
    val title: String,
    val price: String?,
    val available: Boolean?
)

data class FammeResponse(
    val products: List<FammeProduct>
)