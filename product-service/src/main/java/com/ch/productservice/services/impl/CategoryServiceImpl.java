package com.ch.productservice.services.impl;

import com.ch.core.chcore.exceptions.BadRequestException;
import com.ch.core.chcore.exceptions.NotFoundException;
import com.ch.core.chcore.logs.WriteLog;
import com.ch.productservice.api.models.requests.CategoryRequest;
import com.ch.productservice.api.models.responses.CategoryResponse;
import com.ch.productservice.persistence.repositories.CategoryRepository;
import com.ch.productservice.services.contracts.CategoryService;
import com.ch.productservice.utils.mappers.CategoryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the CategoryService interface.
 * This service handles operations related to product categories,
 * including adding, updating, deleting, and retrieving categories.
 * It uses a CategoryRepository for database interactions.
 *
 * @author caito
 *
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    /**
     * Adds a new category to the system.
     * Validates the category request before saving it to the repository.
     *
     * @param request The category request containing the details of the category to be added.
     * @throws BadRequestException if the category request is invalid or if a category with the same name already exists.
     */
    @Override
    @Transactional
    public void addCategory(CategoryRequest request) {
        log.info(WriteLog.logInfo("--> Adding new category service"));
        this.validateCategory(request);
        log.info(WriteLog.logInfo("--> Category request validated successfully keeping.."));
        categoryRepository.save(CategoryMapper.mapToEntity(request));
    }


    /**
     * Fetches all categories from the repository.
     *
     * @return A list of CategoryResponse objects representing all categories.
     */
    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> findAll() {
    log.info(WriteLog.logInfo("--> Fetching all categories"));
        return categoryRepository.findAll()
                .stream()
                .map(CategoryMapper::mapToDto)
                .toList();
    }

    /**
     * Fetches a category by its ID.
     *
     * @param id The ID of the category to be fetched.
     * @return A CategoryResponse object representing the category with the specified ID.
     * @throws NotFoundException if no category with the specified ID exists.
     */
    @Override
    @Transactional(readOnly = true)
    public CategoryResponse findById(Long id) {
        log.info(WriteLog.logInfo("--> Fetching category by id: " + id));
        return categoryRepository.findById(id)
                .map(CategoryMapper::mapToDto)
                .orElseThrow(() -> {
                    log.error(WriteLog.logError("--> Category not found with id: " + id));
                    return new NotFoundException("Category with id '" + id + "' not found");
                });
    }

    /**
     * Fetches a category by its name.
     *
     * @param name The name of the category to be fetched.
     * @return A CategoryResponse object representing the category with the specified name.
     * @throws NotFoundException if no category with the specified name exists.
     */
    @Override
    @Transactional(readOnly = true)
    public CategoryResponse findByName(String name) {
        log.info(WriteLog.logInfo("--> Fetching category by name: " + name));
        return categoryRepository.findByNameContainingIgnoreCase(name)
                .map(CategoryMapper::mapToDto)
                .orElseThrow(() -> {
                    log.error(WriteLog.logError("--> Category not found with name: " + name));
                    return new NotFoundException("Category with name '" + name + "' not found");
                });
    }

    /**
     * Updates an existing category.
     * Validates the category request before updating the category in the repository.
     *
     * @param id      The ID of the category to be updated.
     * @param request The category request containing the updated details of the category.
     * @throws NotFoundException if no category with the specified ID exists.
     * @throws BadRequestException if the category request is invalid or if a category with the same name already exists.
     */
    @Override
    @Transactional
    public void updateCategory(Long id, CategoryRequest request) {
        log.info(WriteLog.logInfo("--> Updating category with id: " + id));
        var category = categoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.error(WriteLog.logError("--> Category not found with id: " + id));
                    return new NotFoundException("Category with id '" + id + "' not found");
                });
        this.validateCategory(request);
        category.setName(request.getName());
        categoryRepository.save(category);
    }

    /**
     * Deletes a category by its ID.
     *
     * @param id The ID of the category to be deleted.
     * @throws NotFoundException if no category with the specified ID exists.
     */
    @Override
    @Transactional
    public void deleteCategory(Long id) {
        log.info(WriteLog.logInfo("--> Deleting category with id: " + id));
        var category = this.findById(id);
        categoryRepository.deleteById(category.getId());
    }

    /**
     * Validates the category request.
     * Checks if the category name is not empty and if a category with the same name already exists.
     *
     * @param request The category request to be validated.
     * @throws BadRequestException if the category name is empty or if a category with the same name already exists.
     */
    private void validateCategory(CategoryRequest request) {
        var errors = new ArrayList<String>();
        log.info(WriteLog.logInfo("--> Validating category request..."));

        if (request.getName() == null || request.getName().isEmpty()) {
            errors.add("Category name cannot be empty");
        }

        if(categoryRepository.existsByName(request.getName())) {
            errors.add("Category with name '" + request.getName() + "' already exists");
        }

        if(!errors.isEmpty()) {
            log.error(WriteLog.logError("--> Category request validation failed: " + errors));
            throw new BadRequestException(errors);
        }
    }
}
