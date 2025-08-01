package com.inventory.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password; // Remember to hash this in a real application!
    private String role; // e.g., "USER", "ADMIN"

    // One User can have many Orders
    // mappedBy points to the 'user' field in the Order entity
    // CascadeType.ALL means if a User is deleted, their Orders are also deleted
    // orphanRemoval = true helps clean up child entities if they are removed from the collection
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    // Helper method to add an order to the user's list
    public void addOrder(Order order) {
        orders.add(order);
        order.setUser(this);
    }

    // Helper method to remove an order from the user's list
    public void removeOrder(Order order) {
        orders.remove(order);
        order.setUser(null);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
