package com.kisahy.commerce.product.adapter.out.persistence.entity;

import java.time.LocalDateTime;

import com.kisahy.commerce.product.domain.model.Product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "products")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private int stockQuantity;

    @Column(nullable = false)
    private int status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    protected ProductEntity() {
    }

    public static ProductEntity fromDomain(Product product) {
        ProductEntity entity = new ProductEntity();

        if (product.getId() != null) {
            entity.id = product.getId();
        }

        entity.name = product.getName();
        entity.description = product.getDescription();
        entity.price = product.getPrice();
        entity.stockQuantity = product.getStockQuantity();
        entity.status = product.getStatus();
        entity.createdAt = product.getCreatedAt();
        entity.updatedAt = product.getUpdatedAt();

        return entity;
    }

    public Product toDomain() {
        return new Product(id, name, description, price, stockQuantity, status, createdAt, updatedAt);
    }
}
