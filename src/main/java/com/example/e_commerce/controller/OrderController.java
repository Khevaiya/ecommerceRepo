package com.example.e_commerce.controller;
import com.example.e_commerce.entity.Order;
import com.example.e_commerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Place an order
    @PostMapping("/{userId}/placeorder")
    public ResponseEntity<Order> placeOrder(@PathVariable String userId,
                                            @RequestBody Order orderRequestDTO) {
        Order order = orderService.placeOrder(userId, orderRequestDTO.getShippingAddress());
        return ResponseEntity.ok(order);
    }

    // Get all orders
    @GetMapping("/getallorders")
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    // Get orders by customer ID
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Order>> getOrdersByCustomerId(@PathVariable String customerId) {
        List<Order> orders = orderService.getOrdersByCustomerId(customerId);
        return ResponseEntity.ok(orders);
    }

    // Get order by order ID
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable String orderId) {
        Optional<Order> order = orderService.getOrderById(orderId);
        return order.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
