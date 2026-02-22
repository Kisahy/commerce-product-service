package com.kisahy.commerce.product.application.port.out;

import com.kisahy.commerce.product.domain.model.Product;

public interface SaveProductPort {
    Product save(Product product);
}
