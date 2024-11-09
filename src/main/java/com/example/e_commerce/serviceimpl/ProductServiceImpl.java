package com.example.e_commerce.serviceimpl;
import com.example.e_commerce.entity.Product;
import com.example.e_commerce.repository.ProductRepository;
import com.example.e_commerce.service.ProductService;
import org.springframework.stereotype.Service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }
    // Check if a product exists by its ID
    public boolean productExists(String productId) {
        return productRepository.existsById(productId); // Returns true if the product exists in the DB
    }

    // Check if the stock is available for the given product and quantity
    public boolean isStockAvailable(String productId, int quantity) {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            return product.get().getStockQuantity() >= quantity; // Check if the stock is sufficient
        }
        return false;
    }
    @Override
    public Product updateProduct(String productId, Product product) {
        product.setId(productId);
        return productRepository.save(product);
    }

    @Override
    public boolean deleteProduct(String productId) {
        if (productRepository.existsById(productId)) {
            productRepository.deleteById(productId);
            return true;
        }
        return false;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> getProductById(String productId) {
        return productRepository.findById(productId);
    }
}
