package com.inventory.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Entity
@Table(name = "order_items") // This is the junction table for Order <-> Product
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many OrderItems belong to one Order
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false) // Foreign key to the 'orders' table
    private Order order;

    // Many OrderItems refer to one Product
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false) // Foreign key to the 'products' table
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    // Store the price of the product at the time of order
    // This is crucial because product prices can change over time
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal priceAtOrder;
}
