package com.kkumta.timedeal.api.dto.product;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ResponseProductDto {
    
    private String sellerName;
    private String productName;
    private Long price;
    private String explanation;
    private Long quantity;
    private Long maximumPurchaseQuantity;
    private LocalDateTime openDate;
    private LocalDateTime closeDate;
    private Boolean isSellingPaused;
}
