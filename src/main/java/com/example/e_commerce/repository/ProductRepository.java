package com.example.e_commerce.repository;
import com.example.e_commerce.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProductRepository extends MongoRepository<Product, String> {
    boolean existsById(String productId);

    // Find a product by its ID
    Optional<Product> findById(String productId);
}
