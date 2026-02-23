package com.kisahy.commerce.product.adapter.in.web;

import java.time.LocalDateTime;

import com.kisahy.commerce.product.domain.model.Product;

public record ProductResponse(
        Long id,
        String name,
        String description,
        Double price,
        int stockQuantity,
        int status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getStatus(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
