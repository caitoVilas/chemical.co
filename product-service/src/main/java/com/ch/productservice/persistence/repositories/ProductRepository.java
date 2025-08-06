package com.ch.productservice.persistence.repositories;

import com.ch.productservice.persistence.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing Product entities.
 * Provides methods to perform CRUD operations and custom queries on Product data.
 * Extends JpaRepository to leverage Spring Data JPA features.
 *
 * @author Your Name
 *
 */
public interface ProductRepository extends JpaRepository<Product,Long> {
    Optional<Product> findBySku(String sku);
    List<Product> findAllByCategory_Id(Long id);
}
