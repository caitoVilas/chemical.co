package com.ch.productservice.api.controllers.impl;

import com.ch.core.chcore.logs.WriteLog;
import com.ch.productservice.api.controllers.contracts.ProductController;
import com.ch.productservice.api.models.requests.ProductRequest;
import com.ch.productservice.api.models.responses.ProductResponse;
import com.ch.productservice.services.contracts.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Implementation of the ProductController interface for managing products.
 * This controller handles HTTP requests related to product creation and retrieval.
 * It uses the ProductService to perform business logic operations.
 *
 * @author Your Name
 *
 */
@RestController
@RequestMapping("/v1/products")
@RequiredArgsConstructor
@Tag(name = "Product API", description = "Product management")
@Slf4j
public class ProductControllerImpl implements ProductController {
    private final ProductService productService;

    @Override
    public ResponseEntity<?> createProduct(ProductRequest request) {
        productService.addProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<List<ProductResponse>> getProducts() {
        var products = productService.getProducts();
        if (products.isEmpty()) {
            log.warn(WriteLog.logWarning("No products found"));
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(products);
    }

    @Override
    public ResponseEntity<List<ProductResponse>> getProductsByCategory(Long categoryId) {
        var products = productService.getProductsByCategoryId(categoryId);
        if (products.isEmpty()){
            log.warn(WriteLog.logWarning("Products not found for category ID: " + categoryId));
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(products);
    }

    @Override
    public ResponseEntity<ProductResponse> getProductById(Long productId) {
        return ResponseEntity.ok(productService.getProductById(productId));
    }

    @Override
    public ResponseEntity<ProductResponse> changeStock(Long productId, Integer stock) {
        return ResponseEntity.ok(productService.changeStock(productId, stock));
    }

    @Override
    public ResponseEntity<ProductResponse> changePrice(Long productId, Double price) {
        return ResponseEntity.ok(productService.changePrice(productId, price));
    }

    @Override
    public ResponseEntity<Void> deleteProduct(Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }
}
