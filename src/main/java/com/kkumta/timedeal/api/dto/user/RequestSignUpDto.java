package com.kkumta.timedeal.api.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RequestSignUpDto {
    
    private String name;
    
    private String email;
    
    private String password;
    
    private String type;
    
    private String phoneNumber;
    
    private String address;
}