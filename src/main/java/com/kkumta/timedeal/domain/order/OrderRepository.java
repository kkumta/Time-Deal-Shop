package com.kkumta.timedeal.domain.order;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {
    
    Page<Order> findAllByBuyerIdAndCreateTimeBetween(Long buyerId, LocalDateTime start,
                                                     LocalDateTime end,
                                                     Pageable pageable);
    
    @Query("select distinct o.buyerId from Order o where o.product.id = :productId")
    List<Long> findUserIdsByProductId(Long productId);
}
