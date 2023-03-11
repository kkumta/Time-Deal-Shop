package com.kkumta.timedeal.exception.product;

public class ProductNotFoundException extends ProductException {
    
    private static final String message = "잘못된 상품 ID 입니다.";
    
    public ProductNotFoundException() {
        super(message);
    }
}
