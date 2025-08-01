package com.inventory.repository;



import com.inventory.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // Optional: Add findByName if needed
    Optional<Product> findByName(String name);
}
