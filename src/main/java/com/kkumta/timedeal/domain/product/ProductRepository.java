package com.kkumta.timedeal.domain.product;

import java.time.LocalDateTime;
import javax.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    Page<Product> findAllByIsDeletedFalseAndIsSellingPausedFalseAndQuantityIsGreaterThanAndOpenDateLessThanEqualAndCloseDateAfter(
        Long quantity, LocalDateTime openDate, LocalDateTime closeDate, Pageable pageable);
    
    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    <S extends Product> S save(S entity);
}