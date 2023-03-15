package com.kkumta.timedeal.exception.user;

public class UserNotFoundException extends UserException {
    
    private static String message = "해당 User는 존재하지 않습니다.";
    
    public UserNotFoundException() {
        super(message);
    }
}
