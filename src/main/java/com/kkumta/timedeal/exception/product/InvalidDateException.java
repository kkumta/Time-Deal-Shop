package com.kkumta.timedeal.exception.product;

public class InvalidDateException extends ProductException {
    
    private static final String message = "CloseDate는 OpenDate보다 10분 이상 뒤여야 합니다. 즉, 상품이 오픈된 상태로 10분 이상 유지될 수 있어야 합니다.";
    
    public InvalidDateException() {
        super(message);
    }
}