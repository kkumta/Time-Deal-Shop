package com.kkumta.timedeal.domain.order;

import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    Page<Order> findAllByBuyerIdAndCreateTimeBetween(Long buyerId, LocalDateTime start,
                                           LocalDateTime end,
                                           Pageable pageable);
    
}
