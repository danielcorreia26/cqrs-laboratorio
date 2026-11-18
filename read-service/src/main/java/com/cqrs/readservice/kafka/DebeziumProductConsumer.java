package com.cqrs.readservice.kafka;

import com.cqrs.readservice.document.ProductDocument;
import com.cqrs.readservice.repository.ProductMongoRepository;
import com.cqrs.readservice.service.ProductSummaryService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

@Component
public class DebeziumProductConsumer {

    private final ProductMongoRepository productMongoRepository;
    private final ProductSummaryService productSummaryService;
    private final ObjectMapper objectMapper;

    public DebeziumProductConsumer(
            ProductMongoRepository productMongoRepository,
            ProductSummaryService productSummaryService,
            ObjectMapper objectMapper) {
        this.productMongoRepository = productMongoRepository;
        this.productSummaryService = productSummaryService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(
            topics = "${app.cqrs.topic}",
            containerFactory = "kafkaListenerContainerFactory"
    )
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

        ProductDocument product = new ProductDocument(
                String.valueOf(id),
                name,
                price
        );

        productMongoRepository.save(product);
        productSummaryService.createOrUpdate(id, name, price);
    }

    private void delete(JsonNode before) {
        if (before.isMissingNode() || before.isNull()) {
            return;
        }

        Long id = before.path("id").asLong();
        productMongoRepository.deleteById(String.valueOf(id));
        productSummaryService.delete(id);
    }
}