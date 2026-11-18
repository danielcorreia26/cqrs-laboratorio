package com.cqrs.readservice.repository;

import com.cqrs.readservice.document.ProductSummaryDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductSummaryMongoRepository
        extends MongoRepository<ProductSummaryDocument, String> {

    List<ProductSummaryDocument> findAllByOrderByIdAsc();

    List<ProductSummaryDocument> findByPriceCategoryOrderByIdAsc(
            String priceCategory
    );
}