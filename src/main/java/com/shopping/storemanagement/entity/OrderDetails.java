package com.shopping.storemanagement.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class OrderDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private double totalPrice;

    private PaymentType paymentType;

    private LocalDateTime creationOn;

    private LocalDateTime modifiedOn;

    @OneToMany(mappedBy = "orderDetails", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems;
}