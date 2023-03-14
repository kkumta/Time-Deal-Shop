package com.kkumta.timedeal.exception.order;

public class MaxQuantityExceededException extends OrderException {
    
    public static String message = "1회 최대 구매 수량을 초과하는 수량입니다.";
    
    public MaxQuantityExceededException() {
        super(message);
    }
}
