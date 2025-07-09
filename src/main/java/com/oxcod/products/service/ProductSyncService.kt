package com.oxcod.products.service

import org.slf4j.LoggerFactory
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate
import com.oxcod.products.dto.FammeResponse
import com.oxcod.products.model.Product
import com.oxcod.products.model.Variant
import com.oxcod.products.repository.ProductRepository
import com.oxcod.products.repository.VariantRepository
import java.util.concurrent.ConcurrentHashMap

@Service
class ProductSyncService(
    private val productRepository: ProductRepository,
    private val variantRepository: VariantRepository,
    restTemplateBuilder: RestTemplateBuilder
) {
    private val logger = LoggerFactory.getLogger(ProductSyncService::class.java)
    private val restTemplate: RestTemplate = restTemplateBuilder.build()
    
    // Cache to store synchronized product IDs during application lifecycle
    private val syncedProductIds = ConcurrentHashMap.newKeySet<Long>()
    private val syncedVariantIds = ConcurrentHashMap.newKeySet<Long>()
    
    companion object {
        private const val FAMME_API_URL = "https://famme.no/products.json"
        private const val MAX_PRODUCTS = 50
    }
    
    init {
        // Load existing product IDs into cache on startup
        loadExistingIds()
    }
    
    private fun loadExistingIds() {
        try {
            val existingProducts = productRepository.findAll()
            syncedProductIds.addAll(existingProducts.map { it.id })
            
            val existingVariants = variantRepository.findAll()
            syncedVariantIds.addAll(existingVariants.map { it.id })
            
            logger.info("Loaded ${syncedProductIds.size} existing products and ${syncedVariantIds.size} existing variants into cache")
        } catch (e: Exception) {
            logger.error("Error loading existing IDs into cache", e)
        }
    }

    @Scheduled(fixedDelay = 3600000, initialDelay = 0) // Run every hour, start immediately
    @Transactional
    fun syncProducts() {
        logger.info("Starting product sync from Famme API")
        
        try {
            val response = restTemplate.getForObject(FAMME_API_URL, FammeResponse::class.java)
            
            if (response != null && response.products.isNotEmpty()) {
                // Take only first 50 products
                val productsToProcess = response.products.take(MAX_PRODUCTS)
                
                // Filter out already synced products
                val newProducts = mutableListOf<Product>()
                val newVariants = mutableListOf<Variant>()
                var skippedProducts = 0
                
                productsToProcess.forEach { fammeProduct ->
                    if (!syncedProductIds.contains(fammeProduct.id)) {
                        // New product - add to list
                        newProducts.add(Product(
                            id = fammeProduct.id,
                            title = fammeProduct.title,
                            productType = fammeProduct.productType,
                            tags = fammeProduct.tags
                        ))
                        
                        // Add this product's variants
                        fammeProduct.variants.forEach { fammeVariant ->
                            if (!syncedVariantIds.contains(fammeVariant.id)) {
                                newVariants.add(Variant(
                                    id = fammeVariant.id,
                                    productId = fammeProduct.id,
                                    title = fammeVariant.title,
                                    price = fammeVariant.price,
                                    available = fammeVariant.available
                                ))
                            }
                        }
                    } else {
                        skippedProducts++
                    }
                }
                
                // Batch save new products and variants
                if (newProducts.isNotEmpty()) {
                    productRepository.saveAll(newProducts)
                    // Add new product IDs to cache
                    syncedProductIds.addAll(newProducts.map { it.id })
                }
                
                if (newVariants.isNotEmpty()) {
                    variantRepository.saveAll(newVariants)
                    // Add new variant IDs to cache
                    syncedVariantIds.addAll(newVariants.map { it.id })
                }
                
                logger.info("Sync completed: ${newProducts.size} new products, ${newVariants.size} new variants, $skippedProducts skipped (already synced)")
            } else {
                logger.warn("No products found in Famme API response")
            }
        } catch (e: Exception) {
            logger.error("Error syncing products from Famme API", e)
        }
    }
}