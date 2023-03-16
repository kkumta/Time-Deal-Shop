package com.kkumta.timedeal.api.dto.user;

import com.kkumta.timedeal.domain.User;
import com.kkumta.timedeal.domain.UserType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class ResponseUserDto {
    
    private String name;
    private String email;
    private UserType type;
    private String phoneNumber;
    private String address;
    
    public static ResponseUserDto of(User user) {
        return ResponseUserDto.builder()
            .name(user.getName())
            .email(user.getEmail())
            .type(user.getType())
            .phoneNumber(user.getPhoneNumber())
            .address(user.getAddress())
            .build();
    }
}
