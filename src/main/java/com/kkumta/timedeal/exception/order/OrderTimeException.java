package com.kkumta.timedeal.exception.order;

public class OrderTimeException extends OrderException {
    
    public static String message = "주문 가능한 시각이 아닙니다.";
    
    public OrderTimeException() {
        super(message);
    }
}
