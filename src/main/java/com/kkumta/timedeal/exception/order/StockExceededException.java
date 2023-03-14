package com.kkumta.timedeal.exception.order;

public class StockExceededException extends OrderException {
    
    public static String message = "현재 재고 수량을 초과하는 수량입니다.";
    
    public StockExceededException() {
        super(message);
    }
}
