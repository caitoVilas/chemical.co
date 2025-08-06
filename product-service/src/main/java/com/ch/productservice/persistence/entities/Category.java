package com.ch.productservice.persistence.entities;

import jakarta.persistence.*;
import lombok.*;

/**
 * Represents a category entity in the product service.
 * This class is used to map the category table in the database.
 * It contains fields for the category ID and name.
 * Lombok annotations are used to generate boilerplate code like getters, setters, constructors, and builders.
 *
 * @author caito
 *
 */
@Entity
@Table(name = "categories")
@NoArgsConstructor@AllArgsConstructor
@Getter@Setter@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}
