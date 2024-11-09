package com.example.e_commerce.service;
import com.example.e_commerce.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    Order placeOrder(String userId, String shippingAddress);
    List<Order> getAllOrders();
    List<Order> getOrdersByCustomerId(String customerId);
    Optional<Order> getOrderById(String orderId);
}
