package com.example.e_commerce.serviceimpl;

import com.example.e_commerce.entity.Cart;
import com.example.e_commerce.entity.Order;
import com.example.e_commerce.repository.CartRepository;
import com.example.e_commerce.repository.OrderRepository;
import com.example.e_commerce.repository.ProductRepository;
import com.example.e_commerce.repository.UserRepository;
import com.example.e_commerce.service.OrderService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Order placeOrder(String userId, String shippingAddress) {

        Cart cart = getCartOrThrow(userId);


        Order order = createOrderFromCart(userId, cart, shippingAddress);


        clearCart(cart);

        return order;
    }

    private Cart getCartOrThrow(String userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user ID: " + userId));
    }
    private Order createOrderFromCart(String userId, Cart cart, String shippingAddress) {
        Order order = new Order();
        order.setUserId(userId);
        order.setShippingAddress(shippingAddress);

        // Convert each CartItem in the cart to an OrderItem for the order
        List<Order.OrderItem> orderItems = cart.getItems().stream()
                .map(cartItem -> {
                    Order.OrderItem orderItem = new Order.OrderItem();
                    orderItem.setProductId(cartItem.getProductId());
                    orderItem.setQuantity(cartItem.getQuantity());

                    productRepository.findById(cartItem.getProductId()).ifPresent(product ->
                            orderItem.setPrice(product.getPrice())
                    );

                    return orderItem;
                })
                .toList();

        order.setProducts(orderItems);
        order.setStatus("Placed");

        return orderRepository.save(order);
    }

    private void clearCart(Cart cart) {
        cart.clear();
        cartRepository.save(cart);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> getOrdersByCustomerId(String customerId) {
        return orderRepository.findByUserId(customerId);
    }

    @Override
    public Optional<Order> getOrderById(String orderId) {
        return orderRepository.findById(orderId);
    }
}
