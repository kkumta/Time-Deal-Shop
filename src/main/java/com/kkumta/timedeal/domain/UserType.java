package com.kkumta.timedeal.domain;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserType {
    
    USER, ADMIN;
    
    public static void find(String input) {
        
        Arrays.stream(UserType.values())
            .filter(type -> type.name().equals(input))
            .findAny()
            .orElseThrow(() -> new RuntimeException(
                "해당하는 UserType이 존재하지 않습니다. 올바른 UserType을 입력해 주십시오."));
    }
}