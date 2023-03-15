package com.kkumta.timedeal.api.dto.user;

import com.kkumta.timedeal.domain.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class ResponseUserListDto {
    
    private String name;
    private String email;
    private String phoneNumber;
    
    public static ResponseUserListDto of(User user) {
        return ResponseUserListDto.builder()
            .name(user.getName())
            .email(user.getEmail())
            .phoneNumber(user.getPhoneNumber())
            .build();
    }
}
