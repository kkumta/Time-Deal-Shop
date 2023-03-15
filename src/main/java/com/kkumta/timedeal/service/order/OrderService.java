package com.kkumta.timedeal.service.order;

import com.kkumta.timedeal.api.dto.order.RequestOrderDto;
import com.kkumta.timedeal.api.dto.order.ResponseOrderListDto;
import com.kkumta.timedeal.exception.order.OrderException;
import com.kkumta.timedeal.exception.product.ProductException;
import com.kkumta.timedeal.exception.user.UserException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    
    Long orderProduct(RequestOrderDto requestDto)
        throws ProductException, OrderException, UserException;
    
    Page<ResponseOrderListDto> getOrders(Long buyerId, String startDate, String endDate,
                                         Pageable pageable)
        throws UserException;
}
