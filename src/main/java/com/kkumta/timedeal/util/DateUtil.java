package com.kkumta.timedeal.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateUtil {
    
    static private LocalDateTime start, end;
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
    
    public static LocalDateTime[] stringToDateTime(String startDate, String endDate) {
        try {
            start = LocalDate.parse(startDate, formatter).atStartOfDay();
            end = LocalDate.parse(endDate, formatter).atTime(23, 59, 59, 999999999);
        } catch (RuntimeException e) {
            throw new RuntimeException("유효하지 않은 date 타입입니다.");
        }
        if (start.isAfter(end)) {
            throw new RuntimeException("시작이 끝보다 늦을 수 없습니다.");
        }
        LocalDateTime today = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
        if (start.isAfter(today) || end.truncatedTo(ChronoUnit.DAYS).isAfter(today)) {
            throw new RuntimeException("미래의 주문은 조회할 수 없습니다.");
        }
        return new LocalDateTime[]{start, end};
    }
}
