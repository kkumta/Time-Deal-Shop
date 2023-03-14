package com.kkumta.timedeal.api.dto.order;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RequestOrderDto {
    
    @NotNull
    private Long productId;
    
    @NotBlank
    private String receiverName;
    
    @NotBlank
    private String address;
    
    @NotBlank
    private String receiverContact;
    
    @NotNull
    private Long quantity;
    
    @Builder
    public RequestOrderDto(Long productId, String receiverName, String address,
                           String receiverContact,
                           Long quantity) {
        this.productId = productId;
        this.receiverName = receiverName;
        this.address = address;
        this.receiverContact = receiverContact;
        this.quantity = quantity;
    }
}
