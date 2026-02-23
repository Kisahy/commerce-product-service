package com.kisahy.commerce.product.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kisahy.commerce.product.adapter.out.persistence.entity.ProductEntity;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {
}
