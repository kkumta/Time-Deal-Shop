package com.kkumta.timedeal.exception.product;

public class ProductDeletedException extends ProductException {
    
    private static final String message = "삭제된 상품입니다.";
    
    public ProductDeletedException() {
        super(message);
    }
}