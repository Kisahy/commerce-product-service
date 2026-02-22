package com.kisahy.commerce.product.application.port.out;

import com.kisahy.commerce.product.domain.model.Product;

import java.util.List;

public interface LoadProductPort {
    List<Product> loadAll();
}
