package com.kkumta.timedeal.domain.product;

import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    Page<Product> findAllByIsDeletedFalseAndIsSellingPausedFalseAndQuantityIsGreaterThanAndOpenDateLessThanEqualAndCloseDateAfter(
        Long quantity, LocalDateTime openDate, LocalDateTime closeDate, Pageable pageable);
}