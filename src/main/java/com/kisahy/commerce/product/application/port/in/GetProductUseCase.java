package com.kisahy.commerce.product.application.port.in;

import com.kisahy.commerce.product.domain.model.Product;

public interface GetProductUseCase {
    Product getProduct(Long id);
}