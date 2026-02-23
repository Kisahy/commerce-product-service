package com.kisahy.commerce.product.adapter.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.kisahy.commerce.product.adapter.out.persistence.entity.ProductEntity;
import com.kisahy.commerce.product.application.port.out.DeleteProductPort;
import com.kisahy.commerce.product.application.port.out.LoadProductPort;
import com.kisahy.commerce.product.application.port.out.SaveProductPort;
import com.kisahy.commerce.product.domain.model.Product;

@Component
public class ProductPersistenceAdapter implements
        SaveProductPort,
        LoadProductPort,
        DeleteProductPort {
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

    @Override
    public void deleteById(Long id) {
        productJpaRepository.deleteById(id);
    }

    @Override
    public Optional<Product> loadById(Long id) {
        return productJpaRepository.findById(id).map(ProductEntity::toDomain);
    }
    
    @Override
    public List<Product> loadAll() {
        return productJpaRepository.findAll()
                .stream()
                .map(ProductEntity::toDomain)
                .toList();
    }
}
