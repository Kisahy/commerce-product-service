package com.kisahy.commerce.product.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kisahy.commerce.product.application.port.in.CreateProductUseCase;
import com.kisahy.commerce.product.application.port.in.DeleteProductUseCase;
import com.kisahy.commerce.product.application.port.in.GetProductUseCase;
import com.kisahy.commerce.product.application.port.in.GetProductsUseCase;
import com.kisahy.commerce.product.application.port.in.UpdateProductUseCase;
import com.kisahy.commerce.product.application.port.out.DeleteProductPort;
import com.kisahy.commerce.product.application.port.out.LoadProductPort;
import com.kisahy.commerce.product.application.port.out.SaveProductPort;
import com.kisahy.commerce.product.domain.exception.ProductNotFoundException;
import com.kisahy.commerce.product.domain.model.Product;

@Service
public class ProductService implements
        CreateProductUseCase,
        GetProductsUseCase,
        GetProductUseCase,
        UpdateProductUseCase,
        DeleteProductUseCase {
    private final SaveProductPort saveProductPort;
    private final LoadProductPort loadProductPort;
    private final DeleteProductPort deleteProductPort;

    public ProductService(
            SaveProductPort saveProductPort,
            LoadProductPort loadProductPort,
            DeleteProductPort deleteProductPort
    ) {
        this.saveProductPort = saveProductPort;
        this.loadProductPort = loadProductPort;
        this.deleteProductPort = deleteProductPort;
    }

    @Override
    @Transactional
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
    @Transactional
    public void updateProduct(Long id, UpdateProductCommand command) {
        Product product = findProductById(id);
        product.update(command.name(), command.description(), command.price());
        saveProductPort.save(product);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        findProductById(id);
        deleteProductPort.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getProducts() {
        return loadProductPort.loadAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Product getProduct(Long id) {
        return findProductById(id);
    }

    private Product findProductById(Long id) {
        return loadProductPort.loadById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
}
