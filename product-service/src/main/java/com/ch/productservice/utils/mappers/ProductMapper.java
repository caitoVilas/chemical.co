package com.ch.productservice.utils.mappers;

import com.ch.productservice.api.models.requests.ProductRequest;
import com.ch.productservice.api.models.responses.ProductResponse;
import com.ch.productservice.persistence.entities.Product;

import java.util.UUID;

/**
 * Utility class for mapping between ProductRequest, ProductResponse, and Product entity.
 * This class provides methods to convert API request and response models to the entity model
 * and vice versa.
 *
 * @author Your Name
 *
 */
public class ProductMapper {

    /**
     * Maps a ProductRequest object to a Product entity.
     * Generates a unique SKU for the product.
     *
     * @param request the ProductRequest object to map
     * @return a Product entity populated with data from the request
     */
    public static Product mapToEntity(ProductRequest request){
        return Product.builder()
                .name(request.getName())
                .sku(UUID.randomUUID().toString())
                .description(request.getDescription())
                .content(request.getContent())
                .price(request.getPrice())
                .stock(request.getStock())
                .build();
    }

    /**
     * Maps a Product entity to a ProductResponse object.
     *
     * @param product the Product entity to map
     * @return a ProductResponse object populated with data from the product entity
     */
    public static ProductResponse mapToDto(Product product){
        return ProductResponse.builder()
                .id(product.getId())
                .sku(product.getSku())
                .name(product.getName())
                .description(product.getDescription())
                .content(product.getContent())
                .price(product.getPrice())
                .stock(product.getStock())
                .imageUrl(product.getImageUrl())
                .category(CategoryMapper.mapToDto(product.getCategory()))
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    public static Product mapToEntity(ProductResponse response) {
       return Product.builder()
               .id(response.getId())
                .sku(response.getSku())
                .name(response.getName())
                .description(response.getDescription())
                .content(response.getContent())
                .price(response.getPrice())
                .stock(response.getStock())
                .imageUrl(response.getImageUrl())
                .category(CategoryMapper.mapToEntity(response.getCategory()))
                .createdAt(response.getCreatedAt())
                .updatedAt(response.getUpdatedAt())
               .build();
    }
}
