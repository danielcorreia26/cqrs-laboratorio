package com.cqrs.readservice.service;

import com.cqrs.readservice.document.ProductDocument;
import com.cqrs.readservice.repository.ProductMongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductQueryService {

    private final ProductMongoRepository productMongoRepository;

    public ProductQueryService(
            ProductMongoRepository productMongoRepository) {
        this.productMongoRepository = productMongoRepository;
    }

    public List<ProductDocument> findAll() {
        return productMongoRepository.findAll();
    }

    public Optional<ProductDocument> findById(Long id) {
        return productMongoRepository.findById(String.valueOf(id));
    }
}