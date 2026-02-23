package com.kisahy.commerce.product.application.port.in;

public interface CreateProductUseCase {
    Long createProduct(CreateProductCommand command);
    
    record CreateProductCommand(
            String name,
            String description,
            Double price,
            int stockQuantity
    ) {
    }
}
