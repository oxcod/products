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

    @GetMapping("/search")
    fun searchPage(model: Model): String {
        return "search"
    }
    
    @GetMapping("/search/products")
    fun searchProducts(
        @RequestParam(required = false) query: String?,
        model: Model
    ): String {
        val products = if (query.isNullOrBlank()) {
            emptyList()
        } else {
            productRepository.searchByTitle(query)
        }
        
        val productsWithVariants = products.map { product ->
            val variants = variantRepository.findByProductId(product.id)
            mapOf(
                "product" to product,
                "variants" to variants
            )
        }
        
        model.addAttribute("products", productsWithVariants)
        model.addAttribute("query", query)
        return "fragments/search-results :: search-results"
    }
    
    @GetMapping("/products")
    fun getProducts(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(required = false) query: String?,
        @RequestParam(defaultValue = "id") sortBy: String,
        @RequestParam(defaultValue = "asc") sortOrder: String,
        model: Model
    ): String {
        val pageSize = 4
        val offset = page * pageSize
        
        val products = if (query.isNullOrBlank()) {
            val totalCount = productRepository.count()
            val productList = productRepository.findAllPaginatedSorted(offset, pageSize, sortBy, sortOrder)
            val totalPages = (totalCount + pageSize - 1) / pageSize
            
            model.addAttribute("totalPages", totalPages)
            model.addAttribute("currentPage", page)
            productList
        } else {
            // For search, show all results with sorting
            productRepository.searchByTitleSorted(query, sortBy, sortOrder)
        }
        
        val productsWithVariants = products.map { product ->
            val variants = variantRepository.findByProductId(product.id)
            mapOf(
                "product" to product,
                "variants" to variants
            )
        }
        
        model.addAttribute("products", productsWithVariants)
        model.addAttribute("query", query ?: "")
        model.addAttribute("sortBy", sortBy)
        model.addAttribute("sortOrder", sortOrder)
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
        return getProducts(0, null, "id", "asc", model)
    }
    
    @GetMapping("/products/{id}/edit")
    fun editProduct(@PathVariable id: Long, model: Model): String {
        val product = productRepository.findById(id)
        if (product == null) {
            return "redirect:/"
        }
        model.addAttribute("product", product)
        return "edit-product"
    }
    
    @PostMapping("/products/{id}/update")
    fun updateProduct(
        @PathVariable id: Long,
        @RequestParam title: String,
        @RequestParam(required = false) productType: String?,
        model: Model
    ): String {
        val existingProduct = productRepository.findById(id)
        if (existingProduct != null) {
            val updatedProduct = existingProduct.copy(
                title = title,
                productType = productType
            )
            productRepository.save(updatedProduct)
        }
        return "redirect:/"
    }
    
    @DeleteMapping("/products/{id}")
    fun deleteProduct(
        @PathVariable id: Long,
        @RequestParam(required = false) query: String?,
        model: Model
    ): String {
        // Delete variants first due to foreign key constraint
        variantRepository.deleteByProductId(id)
        // Then delete the product
        productRepository.deleteById(id)
        
        // If query parameter exists, return search results, otherwise return all products
        return if (!query.isNullOrBlank()) {
            searchProducts(query, model)
        } else {
            getProducts(0, null, "id", "asc", model)
        }
    }
}