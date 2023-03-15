package com.kkumta.timedeal.domain.order;

import java.util.List;

public interface OrderRepositoryCustom {
    
    List<Long> findUserIdsByProductId(Long productId);
}
