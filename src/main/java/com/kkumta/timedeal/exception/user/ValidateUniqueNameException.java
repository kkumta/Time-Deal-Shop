package com.kkumta.timedeal.exception.user;

public class ValidateUniqueNameException extends UserException {
    
    private static String message = "이미 회원가입된 이름입니다.";
    
    public ValidateUniqueNameException() {
        super(message);
    }
}
