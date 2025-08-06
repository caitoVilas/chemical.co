package com.ch.productservice.utils.mappers;

import com.ch.productservice.api.models.requests.CategoryRequest;
import com.ch.productservice.api.models.responses.CategoryResponse;
import com.ch.productservice.persistence.entities.Category;

/**
 * Mapper class for converting between CategoryRequest, CategoryResponse, and Category entity.
 * This class provides static methods to map data between the API layer and the persistence layer.
 *
 * @author caito
 *
 */
public class CategoryMapper {

    /**
     * Maps a CategoryRequest object to a Category entity.
     *
     * @param request the CategoryRequest object to map
     * @return a Category entity with the name from the request
     */
    public static Category mapToEntity(CategoryRequest request){
        return Category.builder()
                .name(request.getName())
                .build();
    }

    /**
     * Maps a Category entity to a CategoryResponse object.
     *
     * @param category the Category entity to map
     * @return a CategoryResponse object with the ID and name from the category
     */
    public static CategoryResponse mapToDto(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public static Category mapToEntity(CategoryResponse response) {
        return Category.builder()
                .id(response.getId())
                .name(response.getName())
                .build();
    }
}
