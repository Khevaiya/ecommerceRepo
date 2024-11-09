package com.example.e_commerce.service;

import com.example.e_commerce.entity.Cart;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public interface CartService {
    Cart addProductToCart(String userId, String productId, int quantity);
    Cart updateCart(String userId, String productId, int quantity);
    Cart removeProductFromCart(String userId, String productId);
    Optional<Cart> getCartByUserId(String userId);
    boolean isProductInCart(String userId, String productId);
}
