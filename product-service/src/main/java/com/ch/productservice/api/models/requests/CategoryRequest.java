package com.ch.productservice.api.models.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Represents a request model for creating or updating a category.
 * This class is used to encapsulate the data required for category operations.
 * It includes fields for the category name.
 * Lombok annotations are used to generate boilerplate code like getters, setters, constructors, and builders.
 *
 * @author caito
 *
 */
@NoArgsConstructor@AllArgsConstructor
@Data@Builder
public class CategoryRequest implements Serializable {
    private String name;
}
