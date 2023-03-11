package com.kkumta.timedeal.exception.product;

public class SellerMismatchException extends ProductException {
    
    private static final String message = "상품의 판매자와 로그인한 계정이 일치하지 않습니다.";
    
    public SellerMismatchException() {
        super(message);
    }
}