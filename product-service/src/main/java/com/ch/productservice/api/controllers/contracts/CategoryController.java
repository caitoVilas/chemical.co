package com.ch.productservice.api.controllers.contracts;

import com.ch.productservice.api.models.requests.CategoryRequest;
import com.ch.productservice.api.models.responses.CategoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This interface defines the contract for the CategoryController,
 * which handles operations related to product categories.
 * It includes methods for adding, retrieving, updating, and deleting categories.
 * Each method is annotated with OpenAPI annotations for documentation and security requirements.
 *
 * @author caito
 *
 */
public interface CategoryController {

    @PostMapping()
    @SecurityRequirement(name = "security token")
    @Operation(description = "Add a new category")
    @Parameter(name = "request", description = "Category request object containing details of the category to be added")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Category added successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> addCategory(@RequestBody CategoryRequest request);

    @GetMapping
    @SecurityRequirement(name = "security token")
    @Operation(description = "Retrieve all categories")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categories retrieved successfully"),
            @ApiResponse(responseCode = "204", description = "No content, no categories found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<CategoryResponse>> getCategories();

    @GetMapping("/id/{id}")
    @SecurityRequirement(name = "security token")
    @Operation(description = "Retrieve category by ID")
    @Parameter(name = "id", description = "ID of the category to retrieve")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categories retrieved successfully"),
            @ApiResponse(responseCode = "204", description = "No content, no categories found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
   public ResponseEntity<CategoryResponse> getCategory(@PathVariable Long id);

    @GetMapping("/name/{name}")
    @SecurityRequirement(name = "security token")
    @Operation(description = "Retrieve category by name")
    @Parameter(name = "name", description = "Name of the category to retrieve")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categories retrieved successfully"),
            @ApiResponse(responseCode = "204", description = "No content, no categories found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<CategoryResponse> getCategoryByName(@PathVariable String name);

    @PutMapping("/{id}")
    @SecurityRequirement(name = "security token")
    @Operation(description = "Update an existing category")
    @Parameters({
            @Parameter(name = "id", description = "ID of the category to update"),
            @Parameter(name = "request", description = "Category request object containing updated details of the category")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid input data"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable Long id, @RequestBody CategoryRequest request);

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "security token")
    @Operation(description = "Delete a category by ID")
    @Parameter(name = "id", description = "ID of the category to delete")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Category deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id);

}
