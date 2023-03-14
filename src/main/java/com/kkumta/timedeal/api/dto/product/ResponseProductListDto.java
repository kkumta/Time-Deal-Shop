package com.kkumta.timedeal.api.dto.product;

import com.kkumta.timedeal.domain.product.Product;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class ResponseProductListDto {
    
    private String sellerName;
    private String productName;
    private Long price;
    private Long quantity;
    private LocalDateTime closeDate;
    
    public static ResponseProductListDto of(Product product) {
        return ResponseProductListDto.builder()
            .sellerName(product.getSellerName())
            .productName(product.getName())
            .price(product.getPrice())
            .quantity(product.getQuantity())
            .closeDate(product.getCloseDate())
            .build();
    }
}