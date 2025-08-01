package com.inventory.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many Orders belong to one User (ManyToOne relationship with User)
    @ManyToOne(fetch = FetchType.LAZY) // Lazy loading is efficient for relationships
    @JoinColumn(name = "user_id", nullable = false) // This creates the 'user_id' foreign key column
    private User user;

    // One Order can have many OrderItems (OneToMany relationship with OrderItem)
    // mappedBy points to the 'order' field in the OrderItem entity
    // CascadeType.ALL ensures operations (e.g., persist, delete) propagate to orderItems
    // orphanRemoval = true ensures that if an OrderItem is removed from this list, it's deleted from DB
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    private LocalDateTime orderDate;

    // Status can be: PENDING, SHIPPED, DELIVERED, CANCELED
    @Column(nullable = false)
    private String status;

    // Total amount of the order, calculated from orderItems
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    // Helper methods for managing order items
    public void addOrderItem(OrderItem item) {
        orderItems.add(item);
        item.setOrder(this);
    }

    public void removeOrderItem(OrderItem item) {
        orderItems.remove(item);
        item.setOrder(null);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
