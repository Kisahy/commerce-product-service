package com.kisahy.commerce.product.application.port.in;

import com.kisahy.commerce.product.domain.model.Product;

import java.util.List;

public interface GetProductsUseCase {
    List<Product> getProducts();
}
