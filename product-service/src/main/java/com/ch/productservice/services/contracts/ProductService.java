package com.ch.productservice.services.contracts;

import com.ch.productservice.api.models.requests.ProductRequest;
import com.ch.productservice.api.models.responses.ProductResponse;

import java.util.List;

/**
 * ProductService interface defines the contract for product-related operations.
 * It includes methods for adding products to the system.
 * This service is part of the product management functionality in the application.
 *
 * @author Your Name
 *
 */
public interface ProductService {

    void addProduct(ProductRequest request);
    List<ProductResponse> getProducts();
    List<ProductResponse> getProductsByCategoryId(Long categoryId);
    ProductResponse changeStock(Long productId, Integer stock);
    ProductResponse changePrice(Long productId, Double price);
    ProductResponse getProductById(Long productId);
    void deleteProduct(Long productId);
}
