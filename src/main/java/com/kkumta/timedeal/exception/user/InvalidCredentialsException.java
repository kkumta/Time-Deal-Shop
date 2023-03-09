package com.kkumta.timedeal.exception.user;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class InvalidCredentialsException extends RuntimeException {
    
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
