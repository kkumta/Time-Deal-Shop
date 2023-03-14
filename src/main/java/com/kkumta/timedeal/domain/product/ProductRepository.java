package com.kkumta.timedeal.domain.product;

import java.time.LocalDateTime;
import java.util.Optional;
import javax.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    @Override
    @Lock(LockModeType.PESSIMISTIC_READ)
    Optional<Product> findById(Long id);
    
    Page<Product> findAllByIsDeletedFalseAndIsSellingPausedFalseAndQuantityIsGreaterThanAndOpenDateLessThanEqualAndCloseDateAfter(
        Long quantity, LocalDateTime openDate, LocalDateTime closeDate, Pageable pageable);
    
    Page<Product> findAllBySellerNameAndCreateTimeBetween(String sellerName, LocalDateTime start,
                                                        LocalDateTime end,
                                                        Pageable pageable);
}