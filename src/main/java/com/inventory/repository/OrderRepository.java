package com.inventory.repository;



import com.inventory.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // Find orders by user ID (useful for user-specific order history)
    List<Order> findByUserId(Long userId); // This method still works because of the @JoinColumn 'user_id'
}
