package com.kisahy.commerce.product.application.service;

import com.kisahy.commerce.product.application.port.in.CreateProductUseCase;
import com.kisahy.commerce.product.application.port.in.GetProductUseCase;
import com.kisahy.commerce.product.application.port.in.GetProductsUseCase;
import com.kisahy.commerce.product.application.port.out.LoadProductPort;
import com.kisahy.commerce.product.application.port.out.SaveProductPort;
import com.kisahy.commerce.product.domain.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService implements
        CreateProductUseCase,
        GetProductsUseCase,
        GetProductUseCase
{
    private final SaveProductPort saveProductPort;
    private final LoadProductPort loadProductPort;

    public ProductService(
            SaveProductPort saveProductPort,
            LoadProductPort loadProductPort
    ) {
        this.saveProductPort = saveProductPort;
        this.loadProductPort = loadProductPort;
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

    @Override
    public List<Product> getProducts() {
        return loadProductPort.loadAll();
    }

    @Override
    public Product getProduct(Long id) {
        return loadProductPort.loadById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("상품을 찾을 수 없습니다.")
                );
    }
}
