package com.kisahy.commerce.product.application.service;

import com.kisahy.commerce.product.application.port.in.CreateProductUseCase;
import com.kisahy.commerce.product.application.port.out.SaveProductPort;
import com.kisahy.commerce.product.domain.model.Product;
import org.springframework.stereotype.Service;

@Service
public class ProductService implements CreateProductUseCase {
    private final SaveProductPort saveProductPort;

    public ProductService(SaveProductPort saveProductPort) {
        this.saveProductPort = saveProductPort;
    }

    @Override
    public Long createProduct(CreateProductCommand command) {
        Product product = Product.create(
                command.name(),
                command.description(),
                command.price(),
                command.stockQuantity()
        );

        Product savedProduct = saveProductPort.save(product);

        return savedProduct.getId();
    }
}
