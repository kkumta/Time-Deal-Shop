package com.kkumta.timedeal.api.dto.order;

import com.kkumta.timedeal.domain.order.Order;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class ResponseOrderListDto {
    
    private Long id;
    private String productName;
    private Long quantity;
    private Long amountPrice;
    private LocalDateTime dateOfPurchase;
    
    public static ResponseOrderListDto of(Order order) {
        return ResponseOrderListDto.builder()
            .id(order.getId())
            .productName(order.getProduct().getName())
            .quantity(order.getQuantity())
            .amountPrice(order.getAmountPrice())
            .dateOfPurchase(order.getCreateTime())
            .build();
    }
}
