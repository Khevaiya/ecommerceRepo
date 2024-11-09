package com.example.e_commerce.controller;



import com.example.e_commerce.entity.Cart;
import com.example.e_commerce.service.CartService;
import com.example.e_commerce.service.ProductService;
import com.example.e_commerce.serviceimpl.CartServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @Autowired
    private ProductService productService;


    // Add a product to the cart
    @PostMapping("/add/{userId}")
    public ResponseEntity<?> addProductToCart(@PathVariable String userId,
                                              @RequestBody Cart.CartItem cartItemRequest) {
        // Validate that the product exists
        if (!productService.productExists(cartItemRequest.getProductId())) {
            return ResponseEntity.badRequest().body("Product ID is invalid or does not exist.");
        }

        // Validate that quantity is positive
        if (cartItemRequest.getQuantity() <= 0) {
            return ResponseEntity.badRequest().body("Quantity must be a positive integer.");
        }

        // Add or update the product in the cart
        Cart cart = cartService.addProductToCart(userId, cartItemRequest.getProductId(), cartItemRequest.getQuantity());
        return ResponseEntity.ok(cart); // Return the updated cart
    }

    // Update a product in the cart
    @PutMapping("/update/{userId}")
    public ResponseEntity<?> updateCart(@PathVariable String userId,
                                        @RequestBody Cart.CartItem cartItemRequest) {
        // Validate that the product exists in the cart
        if (!cartService.isProductInCart(userId, cartItemRequest.getProductId())) {
            return ResponseEntity.badRequest().body("Product is not in the cart.");
        }

        // Validate that the quantity is valid and does not exceed available stock
        if (cartItemRequest.getQuantity() < 0) {
            return ResponseEntity.badRequest().body("Quantity cannot be negative.");
        }

        if (cartItemRequest.getQuantity() == 0) {
            // Remove the product if quantity is zero
            cartService.removeProductFromCart(userId, cartItemRequest.getProductId());
            return ResponseEntity.ok("Product removed from cart.");
        }

        // Ensure the quantity does not exceed available stock
        if (!productService.isStockAvailable(cartItemRequest.getProductId(), cartItemRequest.getQuantity())) {
            return ResponseEntity.badRequest().body("Insufficient stock available.");
        }

        // Update the cart
        Cart cart = cartService.updateCart(userId, cartItemRequest.getProductId(), cartItemRequest.getQuantity());
        return ResponseEntity.ok(cart); // Return the updated cart
    }

    // Remove a product from the cart
    @DeleteMapping("/delete/{userId}/{productId}")
    public ResponseEntity<?> removeProductFromCart(@PathVariable String userId,
                                                   @PathVariable String productId) {
        // Validate that the product exists in the cart
        if (!cartService.isProductInCart(userId, productId)) {
            return ResponseEntity.badRequest().body("Product not found in cart.");
        }

        // Remove the product from the cart
        Cart cart = cartService.removeProductFromCart(userId, productId);
        return ResponseEntity.ok(cart); // Return the updated cart
    }

    // Get the cart by userId
    @GetMapping("/{userId}")
    public ResponseEntity<?> getCartByUserId(@PathVariable String userId) {
        Optional<Cart> cart = cartService.getCartByUserId(userId);
        if (cart.isPresent()) {
            return ResponseEntity.ok(cart.get());
        } else {
            return ResponseEntity.status(404).body("Cart is empty or user not found.");
        }
    }
}
