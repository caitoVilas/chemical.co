package com.ch.productservice.api.models.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Represents a request model for creating or updating a product.
 * This class is used to transfer product data from the client to the server.
 * It includes fields for product details such as name, description, content, price, and stock.
 * Uses Lombok annotations for boilerplate code reduction.
 *
 * @author Your Name
 *
 */
@NoArgsConstructor@AllArgsConstructor
@Data@Builder
public class ProductRequest implements Serializable{
    private String name;
    private String description;
    private String content;
    private Double price;
    private Integer stock;
    private Long categoryId;
}
