package com.cqrs.readservice.controller;

import com.cqrs.readservice.document.ProductDocument;
import com.cqrs.readservice.dto.ProductSummaryResponse;
import com.cqrs.readservice.service.ProductQueryService;
import com.cqrs.readservice.service.ProductSummaryQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductQueryController {

    private final ProductQueryService productQueryService;
    private final ProductSummaryQueryService productSummaryQueryService;

    public ProductQueryController(
            ProductQueryService productQueryService,
            ProductSummaryQueryService productSummaryQueryService) {
        this.productQueryService = productQueryService;
        this.productSummaryQueryService = productSummaryQueryService;
    }

    @GetMapping
    public List<ProductDocument> findAll() {
        return productQueryService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDocument> findById(@PathVariable Long id) {
        return productQueryService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/summary")
    public List<ProductSummaryResponse> findAllSummary(
            @RequestParam(required = false) String category) {
        return productSummaryQueryService.findAll(category);
    }
}