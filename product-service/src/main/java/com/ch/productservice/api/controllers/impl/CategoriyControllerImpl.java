package com.ch.productservice.api.controllers.impl;

import com.ch.core.chcore.logs.WriteLog;
import com.ch.productservice.api.controllers.contracts.CategoryController;
import com.ch.productservice.api.models.requests.CategoryRequest;
import com.ch.productservice.api.models.responses.CategoryResponse;
import com.ch.productservice.services.contracts.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Implementation of the CategoryController interface.
 * This controller handles HTTP requests related to product categories,
 * including adding, retrieving, updating, and deleting categories.
 * It uses a CategoryService for business logic operations.
 *
 * @author caito
 *
 */
@RestController
@RequestMapping("/v1/ctegories")
@RequiredArgsConstructor
@Tag(name = "Category API", description = "Operations related to product categories")
@Slf4j
public class CategoriyControllerImpl implements CategoryController {
    private final CategoryService categoryService;

    @Override
    public ResponseEntity<?> addCategory(CategoryRequest request) {
        categoryService.addCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<List<CategoryResponse>> getCategories() {
        List<CategoryResponse> categories = categoryService.findAll();
        if (categories.isEmpty()) {
            log.warn(WriteLog.logWarning("No categories found"));
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(categories);
    }

    @Override
    public ResponseEntity<CategoryResponse> getCategory(Long id) {
        return ResponseEntity.ok(categoryService.findById(id));
    }

    @Override
    public ResponseEntity<CategoryResponse> getCategoryByName(String name) {
        return ResponseEntity.ok(categoryService.findByName(name));
    }

    @Override
    public ResponseEntity<CategoryResponse> updateCategory(Long id, CategoryRequest request) {
        categoryService.updateCategory(id, request);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> deleteCategory(Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
