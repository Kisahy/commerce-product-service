package com.kisahy.commerce.product.adapter.in.web;

import com.kisahy.commerce.product.application.port.in.CreateProductUseCase;
import com.kisahy.commerce.product.domain.model.Product;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final CreateProductUseCase createProductUseCase;

    public ProductController(CreateProductUseCase createProductUseCase) {
        this.createProductUseCase = createProductUseCase;
    }

    @PostMapping
    public ResponseEntity<Void> createProduct(
            @RequestBody
            @Valid
            CreateProductRequest request
    ) {
        Long productId = createProductUseCase.createProduct(
                new CreateProductUseCase.CreateProductCommand(
                        request.name(),
                        request.description(),
                        request.price(),
                        request.stockQuantity()
                )
        );

        return ResponseEntity.created(URI.create("/products/" + productId)).build();
    }
}
