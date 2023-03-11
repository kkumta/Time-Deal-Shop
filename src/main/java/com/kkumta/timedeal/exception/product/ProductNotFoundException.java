package com.kkumta.timedeal.exception.product;

public class ProductNotFoundException extends IllegalArgumentException {
    
    private static final String message = "존재하지 않는 상품입니다.";
    
    public ProductNotFoundException() {
        super(message);
    }
}
