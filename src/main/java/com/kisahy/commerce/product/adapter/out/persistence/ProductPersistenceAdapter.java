package com.kisahy.commerce.product.adapter.out.persistence;

import com.kisahy.commerce.product.adapter.out.persistence.entity.ProductEntity;
import com.kisahy.commerce.product.application.port.out.SaveProductPort;
import com.kisahy.commerce.product.domain.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductPersistenceAdapter implements SaveProductPort {
    private final ProductJpaRepository productJpaRepository;

    public ProductPersistenceAdapter(ProductJpaRepository productJpaRepository) {
        this.productJpaRepository = productJpaRepository;
    }

    @Override
    public Product save(Product product) {
        ProductEntity entity = ProductEntity.fromDomain(product);
        ProductEntity savedEntity = productJpaRepository.save(entity);

        return savedEntity.toDomain();
    }
}
