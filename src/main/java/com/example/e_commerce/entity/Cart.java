package com.example.e_commerce.entity;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;
import java.util.Optional;

@Data
@Document(collection = "carts")
public class Cart {
    @Id
    private String id;
    private String userId;
    private List<CartItem> items;

    public Cart(String userId) {
        this.userId = userId;
    }

    // Adds an item to the cart or updates the quantity if the item already exists
    public void addItem(String productId, int quantity) {
        Optional<CartItem> existingItem = items.stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            // If the product is already in the cart, update the quantity
            existingItem.get().setQuantity(existingItem.get().getQuantity() + quantity);
        } else {
            // Otherwise, add a new product to the cart
            CartItem newItem = new CartItem();
            newItem.setProductId(productId);
            newItem.setQuantity(quantity);
            items.add(newItem);
        }
    }

    // Updates the quantity of an existing item in the cart
    public void updateItem(String productId, int quantity) {
        CartItem item = items.stream()
                .filter(cartItem -> cartItem.getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item not found in the cart"));

        item.setQuantity(quantity); // Update the quantity of the item
    }

    // Removes an item from the cart
    public void removeItem(String productId) {
        CartItem item = items.stream()
                .filter(cartItem -> cartItem.getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item not found in the cart"));

        items.remove(item); // Remove the item from the cart
    }

    public Cart(String id, String userId, List<CartItem> items) {
        this.id = id;
        this.userId = userId;
        this.items = items;
    }

    // Clears all items in the cart
    public void clear() {
        this.items.clear();
    }

    // CartItem class to hold product and quantity
    @Data
    public static class CartItem {
        private String productId;
        private int quantity;
    }
}
