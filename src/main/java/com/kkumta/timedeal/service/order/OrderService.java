package com.kkumta.timedeal.service.order;

import com.kkumta.timedeal.api.dto.order.RequestOrderDto;
import com.kkumta.timedeal.exception.order.OrderException;
import com.kkumta.timedeal.exception.product.ProductException;

public interface OrderService {
    
    Long orderProduct(RequestOrderDto requestDto)
        throws ProductException, OrderException;
    
}
