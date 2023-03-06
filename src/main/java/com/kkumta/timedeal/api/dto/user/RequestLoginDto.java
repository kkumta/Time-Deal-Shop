package com.kkumta.timedeal.api.dto.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RequestLoginDto {
    
    @Size(min = 5, max = 50)
    @Email
    private String email;
    
    @NotBlank
    @Size(min = 8, max = 50)
    private String password;
}