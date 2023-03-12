package com.kkumta.timedeal.domain.product;

import com.kkumta.timedeal.exception.product.UnsupportedSortConditionException;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SortCondition {
    
    EXPIRING, QUANTITY_ASC;
    
    public static void find(String input) throws UnsupportedSortConditionException {
        Arrays.stream(SortCondition.values())
            .filter(type -> type.name().equals(input))
            .findAny()
            .orElseThrow(() -> new UnsupportedSortConditionException());
    }
}
