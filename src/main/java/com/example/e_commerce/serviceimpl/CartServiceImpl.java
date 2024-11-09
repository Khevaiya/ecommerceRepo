package com.example.e_commerce.serviceimpl;

import com.example.e_commerce.entity.Cart;
import com.example.e_commerce.entity.Product;
import com.example.e_commerce.repository.CartRepository;
import com.example.e_commerce.repository.ProductRepository;
import com.example.e_commerce.service.CartService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service("cartService")
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;
    // Method to check if the product is already in the user's cart
    public boolean isProductInCart(String userId, String productId) {
        Optional<Cart> cartOptional = cartRepository.findByUserId(userId);
        if (cartOptional.isPresent()) {
            Cart cart = cartOptional.get();
            // Check if the product ID is present in the cart
            return cart.getItems().stream().anyMatch(item -> item.getProductId().equals(productId));
        }
        return false; // If the cart is not found, return false
    }

    @Override
    public Cart addProductToCart(String userId, String productId, int quantity) {
        Cart cart = cartRepository.findByUserId(userId).orElse(new Cart(userId));
        cart.addItem(productId, quantity);
        return cartRepository.save(cart);
    }

    @Override
    public Cart updateCart(String userId, String productId, int quantity) {
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Cart not found"));
        cart.updateItem(productId, quantity);
        return cartRepository.save(cart);
    }

    @Override
    public Cart removeProductFromCart(String userId, String productId) {
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Cart not found"));
        cart.removeItem(productId);
        return cartRepository.save(cart);
    }

    @Override
    public Optional<Cart> getCartByUserId(String userId) {
        return cartRepository.findByUserId(userId);
    }
}
