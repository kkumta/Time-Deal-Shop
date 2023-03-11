package com.kkumta.timedeal.api.dto.product;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

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
    
    @Builder
    public ResponseProductDto(String sellerName, String productName, Long price, String explanation,
                              Long quantity, Long maximumPurchaseQuantity, LocalDateTime openDate,
                              LocalDateTime closeDate, Boolean isSellingPaused) {
        this.sellerName = sellerName;
        this.productName = productName;
        this.price = price;
        this.explanation = explanation;
        this.quantity = quantity;
        this.maximumPurchaseQuantity = maximumPurchaseQuantity;
        this.openDate = openDate;
        this.closeDate = closeDate;
        this.isSellingPaused = isSellingPaused;
    }
}
