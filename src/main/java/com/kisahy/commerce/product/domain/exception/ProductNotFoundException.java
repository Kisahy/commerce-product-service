package com.kisahy.commerce.product.domain.exception;

public class ProductNotFoundException extends RuntimeException {
    private final Long productId;

    public ProductNotFoundException(Long productId) {
        super("상품을 찾을 수 없습니다. id: " + productId);
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }
}