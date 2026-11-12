package com.cqrs.writeservice.product;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product create(CreateProductRequest request) {
        Product product = new Product(
                request.name(),
                request.price()
        );

        return productRepository.save(product);
    }
}
