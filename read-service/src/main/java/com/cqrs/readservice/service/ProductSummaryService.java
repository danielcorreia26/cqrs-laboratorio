package com.cqrs.readservice.service;

import com.cqrs.readservice.entity.ProductSummary;
import com.cqrs.readservice.enums.PriceCategory;
import com.cqrs.readservice.repository.ProductSummaryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class ProductSummaryService {

    private final ProductSummaryRepository productSummaryRepository;

    public ProductSummaryService(
            ProductSummaryRepository productSummaryRepository) {
        this.productSummaryRepository = productSummaryRepository;
    }

    @Transactional
    public void createOrUpdate(Long id, String name, BigDecimal price) {
        PriceCategory category = PriceCategory.from(price);
        ProductSummary summary = new ProductSummary(
                id,
                name,
                price,
                category.name()
        );

        productSummaryRepository.save(summary);
    }

    @Transactional
    public void delete(Long id) {
        productSummaryRepository.deleteById(id);
    }
}