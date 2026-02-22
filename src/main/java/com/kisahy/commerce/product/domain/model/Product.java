package com.kisahy.commerce.product.domain.model;

import java.time.LocalDateTime;

public class Product {
    private final Long id;
    private String name;
    private String description;
    private Double price;
    private int stockQuantity;
    private int status;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Product(
            Long id,
            String name,
            String description,
            Double price,
            int stockQuantity,
            int status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Product create(
            String name,
            String description,
            Double price,
            int stockQuantity
    ) {
        return new Product(
                null,
                name,
                description,
                price,
                stockQuantity,
                1,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public int getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
