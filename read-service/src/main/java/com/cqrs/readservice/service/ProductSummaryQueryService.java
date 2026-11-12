package com.cqrs.readservice.service;

import com.cqrs.readservice.dto.ProductSummaryResponse;
import com.cqrs.readservice.entity.ProductSummary;
import com.cqrs.readservice.enums.PriceCategory;
import com.cqrs.readservice.repository.ProductSummaryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductSummaryQueryService {

    private final ProductSummaryRepository productSummaryRepository;

    public ProductSummaryQueryService(
            ProductSummaryRepository productSummaryRepository) {
        this.productSummaryRepository = productSummaryRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductSummaryResponse> findAll(String category) {
        List<ProductSummary> products;
        if (category == null || category.isBlank()) {
            products = productSummaryRepository.findAllByOrderByIdAsc();
        } else {
            PriceCategory priceCategory;
            try {
                priceCategory = PriceCategory.valueOf(category.trim().toUpperCase());
            } catch (IllegalArgumentException exception) {
                throw new IllegalArgumentException(
                        "Categoria inválida. Utilize BUDGET, STANDARD ou PREMIUM."
                );
            }
            products = productSummaryRepository.findByPriceCategoryOrderByIdAsc(priceCategory.name());
        }

        return products.stream()
                .map(product -> new ProductSummaryResponse(
                        product.getId(),
                        product.getName(),
                        product.getPrice(),
                        product.getPriceCategory()
                ))
                .toList();
    }
}