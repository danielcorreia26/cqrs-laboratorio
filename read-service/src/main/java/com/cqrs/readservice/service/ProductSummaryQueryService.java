package com.cqrs.readservice.service;

import com.cqrs.readservice.document.ProductSummaryDocument;
import com.cqrs.readservice.dto.ProductSummaryResponse;
import com.cqrs.readservice.enums.PriceCategory;
import com.cqrs.readservice.repository.ProductSummaryMongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductSummaryQueryService {

    private final ProductSummaryMongoRepository productSummaryMongoRepository;

    public ProductSummaryQueryService(
            ProductSummaryMongoRepository productSummaryMongoRepository) {
        this.productSummaryMongoRepository = productSummaryMongoRepository;
    }

    public List<ProductSummaryResponse> findAll(String category) {
        List<ProductSummaryDocument> products;

        if (category == null || category.isBlank()) {
            products = productSummaryMongoRepository.findAllByOrderByIdAsc();
        } else {
            PriceCategory priceCategory;

            try {
                priceCategory = PriceCategory.valueOf(
                        category.trim().toUpperCase()
                );
            } catch (IllegalArgumentException exception) {
                throw new IllegalArgumentException(
                        "Categoria inválida. Utilize BUDGET, STANDARD ou PREMIUM."
                );
            }

            products = productSummaryMongoRepository
                    .findByPriceCategoryOrderByIdAsc(priceCategory.name());
        }

        return products.stream()
                .map(product -> new ProductSummaryResponse(
                        Long.valueOf(product.getId()),
                        product.getName(),
                        product.getPrice(),
                        product.getPriceCategory()
                ))
                .toList();
    }
}