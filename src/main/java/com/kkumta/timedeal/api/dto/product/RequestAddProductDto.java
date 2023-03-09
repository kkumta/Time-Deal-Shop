package com.kkumta.timedeal.api.dto.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

@AllArgsConstructor
@Getter
public class RequestAddProductDto {
    
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
}