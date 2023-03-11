package com.kkumta.timedeal.api.dto.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import javax.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
public class RequestUpdateProductDto {
    
    @NotBlank
    @Size(min = 2, max = 30)
    private String name;
    
    @NotNull
    @Min(0)
    @Max(100000000)
    private Long price;
    
    @NotBlank
    private String explanation;
    
    @NotNull
    @Min(1)
    @Max(1000000000)
    private Long quantity;
    
    @NotNull
    @Min(1)
    @Max(100000000)
    private Long maximumPurchaseQuantity;
    
    @NotNull
    @Future
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime openDate;
    
    @NotNull
    @Future
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime closeDate;
    
    @NotNull
    private Boolean isSellingPaused;
    
    @Builder
    public RequestUpdateProductDto(String name, Long price, String explanation,
                                   Long quantity,
                                   Long maximumPurchaseQuantity, LocalDateTime openDate,
                                   LocalDateTime closeDate, Boolean isSellingPaused) {
        this.name = name;
        this.price = price;
        this.explanation = explanation;
        this.quantity = quantity;
        this.maximumPurchaseQuantity = maximumPurchaseQuantity;
        this.openDate = openDate;
        this.closeDate = closeDate;
        this.isSellingPaused = isSellingPaused;
    }
}