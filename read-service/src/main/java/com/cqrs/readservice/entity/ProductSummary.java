package com.cqrs.readservice.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "product_summary")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductSummary {

    @Id
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(nullable = false, length = 20)
    private String priceCategory;

    public ProductSummary(
            Long id,
            String name,
            BigDecimal price,
            String priceCategory) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.priceCategory = priceCategory;
    }
}
