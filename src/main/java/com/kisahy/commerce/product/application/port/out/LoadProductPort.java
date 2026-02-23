package com.kisahy.commerce.product.application.port.out;

import com.kisahy.commerce.product.domain.model.Product;

import java.util.List;
import java.util.Optional;

public interface LoadProductPort {
    List<Product> loadAll();

    Optional<Product> loadById(Long id);
}
