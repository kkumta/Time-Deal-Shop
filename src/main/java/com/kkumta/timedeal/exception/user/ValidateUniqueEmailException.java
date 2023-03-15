package com.kkumta.timedeal.exception.user;

public class ValidateUniqueEmailException extends UserException {
    
    private static String message = "이미 회원가입된 이메일입니다.";
    
    public ValidateUniqueEmailException() {
        super(message);
    }
}
