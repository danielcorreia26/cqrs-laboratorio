package com.cqrs.readservice.kafka;

import com.cqrs.readservice.entity.Product;
import com.cqrs.readservice.repository.ProductRepository;
import com.cqrs.readservice.service.ProductSummaryService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

@Component
public class DebeziumProductConsumer {

    private final ProductRepository productRepository;
    private final ProductSummaryService productSummaryService;
    private final ObjectMapper objectMapper;

    public DebeziumProductConsumer(
            ProductRepository productRepository,
            ProductSummaryService productSummaryService,
            ObjectMapper objectMapper) {

        this.productRepository = productRepository;
        this.productSummaryService = productSummaryService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(
            topics = "${app.cqrs.topic}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    @Transactional
    public void consume(String message) throws Exception {
        if (message == null || message.isBlank()) {
            return;
        }

        JsonNode root = objectMapper.readTree(message);
        JsonNode payload = root.path("payload");
        if (payload.isMissingNode() || payload.isNull()) {
            return;
        }

        String operation = payload.path("op").asText("");
        switch (operation) {
            case "r", "c", "u" -> upsert(payload.path("after"));
            case "d" -> delete(payload.path("before"));
            default -> {
                // Ignora operações desconhecidas.
            }
        }
    }

    private void upsert(JsonNode after) {
        if (after.isMissingNode() || after.isNull()) {
            return;
        }

        Long id = after.path("id").asLong();
        String name = after.path("name").asText();
        BigDecimal price = new BigDecimal(after.path("price").asText());

        // Mantém a cópia original na tabela products.
        Product product = new Product(id, name, price);
        productRepository.save(product);

        // Cria ou atualiza a projeção de leitura.
        productSummaryService.createOrUpdate(id, name, price);
    }

    private void delete(JsonNode before) {
        if (before.isMissingNode() || before.isNull()) {
            return;
        }

        Long id = before.path("id").asLong();
        productRepository.deleteById(id);
        productSummaryService.delete(id);
    }
}