package com.ch.productservice.api.controllers.contracts;

import com.ch.productservice.api.models.requests.ProductRequest;
import com.ch.productservice.api.models.responses.ProductResponse;
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
 * Interface defining the contract for product management operations.
 * This interface includes methods for creating a new product and retrieving all products.
 * It is intended to be implemented by a controller class that handles HTTP requests.
 *
 * @author Your Name
 *
 */
public interface ProductController {

    @PostMapping()
    @SecurityRequirement(name = "security token")
    @Operation(description = "Add a new product")
    @Parameter(name = "request", description = "Product request object containing details of the product to be added")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Product added successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> createProduct(@RequestBody ProductRequest request);

    @GetMapping
    @SecurityRequirement(name = "security token")
    @Operation(description = "Retrieve all products")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully"),
            @ApiResponse(responseCode = "204", description = "No content, no categories found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<ProductResponse>> getProducts();

    @GetMapping("/category/{categoryId}")
    @SecurityRequirement(name = "security token")
    @Operation(description = "Retrieve all products by category")
    @Parameter(name = "categoryId", description = "ID of the category to filter products")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully"),
            @ApiResponse(responseCode = "204", description = "No content, no categories found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<ProductResponse>> getProductsByCategory(@PathVariable Long categoryId);

    @GetMapping("/id/{productId}")
    @SecurityRequirement(name = "security token")
    @Operation(description = "Retrieve a product by its ID")
    @Parameter(name = "productId", description = "ID of the product to retrieve")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long productId);


    @PutMapping("/stock")
    @SecurityRequirement(name = "security token")
    @Operation(description = "Change stock of a product")
    @Parameters({
            @Parameter(name = "productId", description = "ID of the product to change stock"),
            @Parameter(name = "stock", description = "New stock value for the product")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Stock changed successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ProductResponse> changeStock(@RequestParam Long productId, @RequestParam Integer stock);

    @PutMapping("/price")
    @SecurityRequirement(name = "security token")
    @Operation(description = "Change price of a product")
    @Parameters({
            @Parameter(name = "productId", description = "ID of the product to change stock"),
            @Parameter(name = "price", description = "New price value for the product")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Stock changed successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ProductResponse> changePrice(@RequestParam Long productId, @RequestParam Double price);

    @DeleteMapping("/{productId}")
    @SecurityRequirement(name = "security token")
    @Operation(description = "Delete a product by its ID")
    @Parameter(name = "productId", description = "ID of the product to delete")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId);
}
