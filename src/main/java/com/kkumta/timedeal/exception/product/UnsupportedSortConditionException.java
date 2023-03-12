package com.kkumta.timedeal.exception.product;

public class UnsupportedSortConditionException extends ProductException {
    
    private static final String message = "지원하지 않는 정렬 조건입니다.";
    
    public UnsupportedSortConditionException() {
        super(message);
    }
}