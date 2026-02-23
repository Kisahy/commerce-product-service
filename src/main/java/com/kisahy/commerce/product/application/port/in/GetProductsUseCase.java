package com.kisahy.commerce.product.application.port.in;

import java.util.List;

import com.kisahy.commerce.product.domain.model.Product;

public interface GetProductsUseCase {
    List<Product> getProducts();
}