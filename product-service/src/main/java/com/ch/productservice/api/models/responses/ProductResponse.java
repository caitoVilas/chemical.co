package com.ch.productservice.api.models.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents a response model for a product.
 * This class is used to transfer product data from the server to the client.
 * It includes fields for product details such as SKU, name, description, price, stock, image URL, and timestamps.
 * Uses Lombok annotations for boilerplate code reduction.
 *
 * @author Your Name
 *
 */
@NoArgsConstructor@AllArgsConstructor
@Data@Builder
public class ProductResponse implements Serializable {
    private Long id;
    private String sku;
    private String name;
    private String description;
    private String content;
    @JsonFormat(shape = JsonFormat.Shape.NUMBER, pattern = "################.##")
    private Double price;
    @JsonFormat(shape = JsonFormat.Shape.NUMBER, pattern = "########")
    private Integer stock;
    private String imageUrl;
    private CategoryResponse category;
    @JsonFormat(shape =  JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(shape =  JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime updatedAt;
}
