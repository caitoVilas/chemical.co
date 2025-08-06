package com.ch.productservice.services.contracts;

import com.ch.productservice.api.models.requests.CategoryRequest;
import com.ch.productservice.api.models.responses.CategoryResponse;

import java.util.List;

/**
 * Service interface for managing product categories.
 * Provides methods to add, retrieve, update, and delete categories.
 *
 * @author caito
 *
 */
public interface CategoryService {

    void addCategory(CategoryRequest request);
    List<CategoryResponse> findAll();
    CategoryResponse findById(Long id);
    CategoryResponse findByName(String name);
    void updateCategory(Long id, CategoryRequest request);
    void deleteCategory(Long id);
}
