package com.ch.productservice.persistence.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Represents a product entity in the product service.
 * This class maps to the "products" table in the database.
 * It contains fields for product details such as SKU, name, description, price, stock, and image URL.
 * It also includes timestamps for creation and last update.
 * Uses Lombok annotations for boilerplate code reduction.
 * Uses JPA annotations for ORM mapping.
 *
 * @author Your Name
 *
 */
@Entity
@Table(name = "products")
@NoArgsConstructor@AllArgsConstructor
@Getter@Setter@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sku;
    private String name;
    private String description;
    private String content;
    private Double price;
    private Integer stock;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    private String imageUrl;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
