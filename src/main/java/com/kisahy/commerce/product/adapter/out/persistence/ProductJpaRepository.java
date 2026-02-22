package com.kisahy.commerce.product.adapter.out.persistence;

import com.kisahy.commerce.product.adapter.out.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {}
