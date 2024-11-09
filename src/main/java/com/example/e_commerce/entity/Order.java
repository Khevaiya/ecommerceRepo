package com.example.e_commerce.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "orders")
public class Order {
    @Id
    private String id;
    private String userId;
    private List<OrderItem> products;
    private String shippingAddress;
    private String status;

   @Data
    public static class OrderItem {
        private String productId;
        private int quantity;
        private double price;


    }


}
