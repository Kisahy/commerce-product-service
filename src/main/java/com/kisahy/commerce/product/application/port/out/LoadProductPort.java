package com.kisahy.commerce.product.application.port.out;

import java.util.List;
import java.util.Optional;

import com.kisahy.commerce.product.domain.model.Product;

public interface LoadProductPort {
    Optional<Product> loadById(Long id);

    List<Product> loadAll();
}
