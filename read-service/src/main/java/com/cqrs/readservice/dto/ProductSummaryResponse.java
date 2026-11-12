package com.cqrs.readservice.dto;

import java.math.BigDecimal;

public record ProductSummaryResponse(
        Long id,
        String name,
        BigDecimal price,
        String priceCategory
) {
}