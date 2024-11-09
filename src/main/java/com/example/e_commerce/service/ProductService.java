package com.example.e_commerce.service;
import com.example.e_commerce.entity.Product;

import java.util.List;
import java.util.Optional;


public interface ProductService {
    Product addProduct(Product product);
    Product updateProduct(String productId, Product product);
    boolean deleteProduct(String productId);
    List<Product> getAllProducts();
    Optional<Product> getProductById(String productId);
    public boolean productExists(String productId);
    public boolean isStockAvailable(String productId, int quantity);




}
