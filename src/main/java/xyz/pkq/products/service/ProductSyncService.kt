package xyz.pkq.products.service

import org.slf4j.LoggerFactory
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate
import xyz.pkq.products.dto.FammeResponse
import xyz.pkq.products.model.Product
import xyz.pkq.products.model.Variant
import xyz.pkq.products.repository.ProductRepository
import xyz.pkq.products.repository.VariantRepository

@Service
class ProductSyncService(
    private val productRepository: ProductRepository,
    private val variantRepository: VariantRepository,
    restTemplateBuilder: RestTemplateBuilder
) {
    private val logger = LoggerFactory.getLogger(ProductSyncService::class.java)
    private val restTemplate: RestTemplate = restTemplateBuilder.build()
    
    companion object {
        private const val FAMME_API_URL = "https://famme.no/products.json"
        private const val MAX_PRODUCTS = 50
    }

    @Scheduled(fixedDelay = 3600000, initialDelay = 0) // Run every hour, start immediately
    @Transactional
    fun syncProducts() {
        logger.info("Starting product sync from Famme API")
        
        try {
            val response = restTemplate.getForObject(FAMME_API_URL, FammeResponse::class.java)
            
            if (response != null && response.products.isNotEmpty()) {
                // Clear existing data
                variantRepository.deleteAll()
                productRepository.deleteAll()
                
                // Take only first 50 products
                val productsToSave = response.products.take(MAX_PRODUCTS)
                
                productsToSave.forEach { fammeProduct ->
                    // Save product
                    val product = Product(
                        id = fammeProduct.id,
                        title = fammeProduct.title,
                        productType = fammeProduct.productType
                    )
                    productRepository.save(product)
                    
                    // Save variants
                    fammeProduct.variants.forEach { fammeVariant ->
                        val variant = Variant(
                            id = fammeVariant.id,
                            productId = fammeProduct.id,
                            title = fammeVariant.title,
                            price = fammeVariant.price,
                            available = fammeVariant.available
                        )
                        variantRepository.save(variant)
                    }
                }
                
                logger.info("Successfully synced ${productsToSave.size} products")
            } else {
                logger.warn("No products found in Famme API response")
            }
        } catch (e: Exception) {
            logger.error("Error syncing products from Famme API", e)
        }
    }
}