package com.ch.productservice.api.models.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Represents a response model for a category.
 * This class is used to encapsulate the data returned when fetching category details.
 * It includes fields for the category ID and name.
 * Lombok annotations are used to generate boilerplate code like getters, setters, constructors, and builders.
 *
 * @author caito
 *
 */
@NoArgsConstructor@AllArgsConstructor
@Data@Builder
public class CategoryResponse implements Serializable {
    private Long id;
    private String name;
}
