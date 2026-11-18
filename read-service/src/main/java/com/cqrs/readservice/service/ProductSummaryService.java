package com.cqrs.readservice.service;

import com.cqrs.readservice.document.ProductSummaryDocument;
import com.cqrs.readservice.enums.PriceCategory;
import com.cqrs.readservice.repository.ProductSummaryMongoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ProductSummaryService {

    private final ProductSummaryMongoRepository productSummaryMongoRepository;

    public ProductSummaryService(
            ProductSummaryMongoRepository productSummaryMongoRepository) {
        this.productSummaryMongoRepository = productSummaryMongoRepository;
    }

    public void createOrUpdate(Long id, String name, BigDecimal price) {
        PriceCategory category = PriceCategory.from(price);

        ProductSummaryDocument summary = new ProductSummaryDocument(
                String.valueOf(id),
                name,
                price,
                category.name()
        );

        productSummaryMongoRepository.save(summary);
    }

    public void delete(Long id) {
        productSummaryMongoRepository.deleteById(String.valueOf(id));
    }
}