package com.kkumta.timedeal.exception.user;

public class LoginInfoNotFoundException extends UserException {
    
    private static final String message = "세션에 로그인 정보가 없습니다.";
    
    public LoginInfoNotFoundException() {
        super(message);
    }
}