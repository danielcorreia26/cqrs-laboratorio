
package com.cqrs.readservice.repository;

import com.cqrs.readservice.entity.ProductSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductSummaryRepository
        extends JpaRepository<ProductSummary, Long> {

    List<ProductSummary> findAllByOrderByIdAsc();

    List<ProductSummary> findByPriceCategoryOrderByIdAsc(String priceCategory);
}
