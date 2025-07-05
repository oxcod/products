package xyz.pkq.products.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import xyz.pkq.products.model.Product
import xyz.pkq.products.repository.ProductRepository
import xyz.pkq.products.repository.VariantRepository

@Controller
class ProductController(
    private val productRepository: ProductRepository,
    private val variantRepository: VariantRepository
) {

    @GetMapping("/")
    fun index(model: Model): String {
        model.addAttribute("showTable", false)
        return "index"
    }

    @GetMapping("/products")
    fun getProducts(model: Model): String {
        val products = productRepository.findAll()
        val productsWithVariants = products.map { product ->
            val variants = variantRepository.findByProductId(product.id)
            mapOf(
                "product" to product,
                "variants" to variants
            )
        }
        
        model.addAttribute("products", productsWithVariants)
        model.addAttribute("showTable", true)
        return "fragments/product-table :: product-table"
    }

    @PostMapping("/products")
    fun addProduct(
        @RequestParam title: String,
        @RequestParam(required = false) productType: String?,
        model: Model
    ): String {
        // Generate a simple ID based on current timestamp
        val newProduct = Product(
            id = System.currentTimeMillis(),
            title = title,
            productType = productType
        )
        
        productRepository.save(newProduct)
        
        // Return updated product list
        return getProducts(model)
    }
}