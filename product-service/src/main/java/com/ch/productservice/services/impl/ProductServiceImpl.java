package com.ch.productservice.services.impl;

import com.ch.core.chcore.exceptions.BadRequestException;
import com.ch.core.chcore.exceptions.NotFoundException;
import com.ch.core.chcore.logs.WriteLog;
import com.ch.productservice.api.models.requests.ProductRequest;
import com.ch.productservice.api.models.responses.ProductResponse;
import com.ch.productservice.persistence.entities.Category;
import com.ch.productservice.persistence.entities.Product;
import com.ch.productservice.persistence.repositories.CategoryRepository;
import com.ch.productservice.persistence.repositories.ProductRepository;
import com.ch.productservice.services.contracts.ProductService;
import com.ch.productservice.utils.mappers.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * ProductServiceImpl is the implementation of the ProductService interface.
 * It provides methods to add products and retrieve a list of products.
 * This service interacts with the product repository and category repository
 * to perform CRUD operations on product entities.
 * It also includes validation logic for product requests.
 *
 * @author Your Name
 *
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    /**
     * Adds a new product to the system.
     * Validates the product request and saves the product entity to the database.
     * If the category associated with the product does not exist, it throws a NotFoundException.
     *
     * @param request The product request containing product details.
     */
    @Override
    @Transactional
    public void addProduct(ProductRequest request) {
        log.info(WriteLog.logInfo("--> adding new product service"));
        this.validateProduct(request);
        log.info(WriteLog.logInfo("--> product request validated successfully keeping.."));
        var category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> {
                    log.error(WriteLog.logError("--> category not found with id: " + request.getCategoryId()));
                    return new NotFoundException("Category not found with id: " + request.getCategoryId());
                });
        var product = ProductMapper.mapToEntity(request);
        product.setCategory(category);
        productRepository.save(product);
    }

    /**
     * Retrieves a list of all products in the system.
     * Maps the product entities to product response DTOs.
     *
     * @return A list of ProductResponse objects representing all products.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getProducts() {
        log.info(WriteLog.logInfo("--> retrieving all products"));
        return productRepository.findAll()
                .stream()
                .map(ProductMapper::mapToDto)
                .toList();
    }

    /**
     * Retrieves a list of products by category ID.
     * Maps the product entities to product response DTOs.
     * If no products are found for the given category ID, it returns an empty list.
     *
     * @param categoryId The ID of the category to filter products.
     * @return A list of ProductResponse objects representing products in the specified category.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getProductsByCategoryId(Long categoryId) {
        log.info(WriteLog.logInfo("--> retrieving products by category id: " + categoryId));
        return productRepository.findAllByCategory_Id(categoryId)
                .stream()
                .map(ProductMapper::mapToDto)
                .toList();
    }

    /**
     * Changes the stock of a product by its ID.
     * Validates the stock value and updates the product entity in the database.
     * If the stock value is invalid, it throws a BadRequestException.
     *
     * @param productId The ID of the product to update.
     * @param stock The new stock value for the product.
     * @return A ProductResponse object representing the updated product.
     */
    @Override
    @Transactional
    public ProductResponse changeStock(Long productId, Integer stock) {
        log.info(WriteLog.logInfo("--> updating product stock with id: " + productId));
        var oldProduct = this.getProductById(productId);
        if (stock != null && stock >= 0) {
            var product = ProductMapper.mapToEntity(oldProduct);
            product.setStock(stock);
            return ProductMapper.mapToDto(productRepository.save(product));
        }else {
            log.error(WriteLog.logError("--> invalid stock value: " + stock));
            throw new BadRequestException(List.of("Invalid stock value: " + stock));
        }
    }

    /**
     * Changes the price of a product by its ID.
     * Validates the price value and updates the product entity in the database.
     * If the price value is invalid, it throws a BadRequestException.
     *
     * @param productId The ID of the product to update.
     * @param price The new price value for the product.
     * @return A ProductResponse object representing the updated product.
     */
    @Override
    @Transactional
    public ProductResponse changePrice(Long productId, Double price) {
        log.info(WriteLog.logInfo("--> updating product price with id: " + productId));
        var oldProduct = this.getProductById(productId);
        if (price != null && price > 0) {
            var product = ProductMapper.mapToEntity(oldProduct);
            product.setPrice(price);
            return ProductMapper.mapToDto(productRepository.save(product));
        } else {
            log.error(WriteLog.logError("--> invalid price value: " + price));
            throw new BadRequestException(List.of("Invalid price value: " + price));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long productId) {
        log.info(WriteLog.logInfo("--> retrieving product by id: " + productId));
        return productRepository.findById(productId)
                .map(ProductMapper::mapToDto)
                .orElseThrow(() -> {
                    log.error(WriteLog.logError("--> product not found with id: " + productId));
                    return new NotFoundException("Product not found with id: " + productId);
                });
    }

    /**
     * Deletes a product by its ID.
     * Retrieves the product to ensure it exists before attempting to delete it.
     * If the product does not exist, it throws a NotFoundException.
     *
     * @param productId The ID of the product to delete.
     */
    @Override
    @Transactional
    public void deleteProduct(Long productId) {
        log.info(WriteLog.logInfo("--> deleting product with id: " + productId));
        var product = this.getProductById(productId);
        productRepository.deleteById(productId);
    }

    /**
     * Validates the product request.
     * Checks for required fields and their values.
     * If validation fails, it throws a BadRequestException with appropriate error messages.
     *
     * @param request The product request to validate.
     */
    private void validateProduct(ProductRequest request) {
        var errors = new ArrayList<String>();
        log.debug(WriteLog.logInfo("--> validating product request..."));

        if (request.getName() == null || request.getName().isEmpty()) {
            errors.add("Product name is required.");
        }
        if (request.getDescription() == null || request.getDescription().isEmpty()) {
            errors.add("Product description is required.");
        }
        if (request.getContent() == null || request.getContent().isEmpty()) {
            errors.add("Product content is required.");
        }
        if (request.getPrice() == null || request.getPrice() <= 0) {
            errors.add("Product price must be greater than zero.");
        }
        if (request.getStock() == null || request.getStock() < 0) {
            errors.add("Product stock cannot be negative.");
        }

        if (!errors.isEmpty()) {
            log.error(WriteLog.logError("--> product request validation failed: " + errors));
            throw new BadRequestException(errors);
        }
    }
}
