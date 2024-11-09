package com.example.e_commerce.controller;

import com.example.e_commerce.entity.Product;
import com.example.e_commerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Add a new product
    @PostMapping("/addproduct")
    public ResponseEntity<?> addProduct(@RequestBody Product product) {
        // Validate input data (e.g., price should be positive, required fields should not be null)
        if (product.getName() == null || product.getName().isEmpty() ||
                product.getDescription() == null || product.getDescription().isEmpty() ||
                product.getPrice() <= 0 || product.getCategory() == null || product.getCategory().isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid input data. Please provide all required fields and ensure price is positive.");
        }

        // Save the product
        Product savedProduct = productService.addProduct(product);

        // Return success message with product ID
        return ResponseEntity.status(201).body("Product added successfully. Product ID: " + savedProduct.getId());
    }

    // Update an existing product
    @PutMapping("/updateproduct/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable String productId, @RequestBody Product product) {
        // Validate that the product exists
        Optional<Product> existingProduct = productService.getProductById(productId);
        if (!existingProduct.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        // Only update fields that are provided
        Product updatedProduct = productService.updateProduct(productId, product);

        // Return success message with updated product details
        return ResponseEntity.ok("Product updated successfully. Product ID: " + updatedProduct.getId());
    }

    // Delete a product
    @DeleteMapping("/deleteproduct/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable String productId) {
        // Verify if the product exists
        Optional<Product> product = productService.getProductById(productId);
        if (!product.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        // Delete the product
        boolean isDeleted = productService.deleteProduct(productId);
        if (isDeleted) {
            return ResponseEntity.ok("Product deleted successfully.");
        } else {
            return ResponseEntity.status(500).body("Error occurred while deleting the product.");
        }
    }

    // Get all products
    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        if (products.isEmpty()) {
            return ResponseEntity.status(404).body("No products found.");
        }
        return ResponseEntity.ok(products);
    }

    // Get a product by its ID
    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable String productId) {
        Optional<Product> product = productService.getProductById(productId);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
