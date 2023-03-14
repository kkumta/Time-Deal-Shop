package com.kkumta.timedeal.domain.order;

import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Embeddable
public class DeliveryInfo {
    
    private String receiverName;
    
    private String address;
    
    private String receiverContact;
    
    public DeliveryInfo(String receiverName, String address, String receiverContact) {
        this.receiverName = receiverName;
        this.address = address;
        this.receiverContact = receiverContact;
    }
}