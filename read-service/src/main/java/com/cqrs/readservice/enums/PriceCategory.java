package com.cqrs.readservice.enums;

import java.math.BigDecimal;

public enum PriceCategory {

    BUDGET,
    STANDARD,
    PREMIUM;

    public static PriceCategory from(BigDecimal price) {

        if (price.compareTo(new BigDecimal("100.00")) <= 0) {
            return BUDGET;
        }

        if (price.compareTo(new BigDecimal("250.00")) <= 0) {
            return STANDARD;
        }

        return PREMIUM;
    }
}