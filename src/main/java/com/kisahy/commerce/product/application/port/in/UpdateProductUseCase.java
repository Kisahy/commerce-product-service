package com.kisahy.commerce.product.application.port.in;

public interface UpdateProductUseCase {
        void updateProduct(Long id, UpdateProductCommand command);

        record UpdateProductCommand(
                String name,
                String description,
                Double price
        ) {}
}
