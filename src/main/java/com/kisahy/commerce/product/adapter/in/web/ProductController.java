package com.kisahy.commerce.product.adapter.in.web;

import com.kisahy.commerce.product.application.port.in.CreateProductUseCase;
import com.kisahy.commerce.product.application.port.in.GetProductUseCase;
import com.kisahy.commerce.product.application.port.in.GetProductsUseCase;
import com.kisahy.commerce.product.application.port.in.UpdateProductUseCase;
import com.kisahy.commerce.product.domain.model.Product;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final CreateProductUseCase createProductUseCase;
    private final GetProductsUseCase getProductsUseCase;
    private final GetProductUseCase getProductUseCase;
    private final UpdateProductUseCase updateProductUseCase;

    public ProductController(
            CreateProductUseCase createProductUseCase,
            GetProductsUseCase getProductsUseCase,
            GetProductUseCase getProductUseCase,
            UpdateProductUseCase updateProductUseCase) {
        this.createProductUseCase = createProductUseCase;
        this.getProductsUseCase = getProductsUseCase;
        this.getProductUseCase = getProductUseCase;
        this.updateProductUseCase = updateProductUseCase;
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

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProduct(
            @PathVariable
            Long id,
            @RequestBody
            @Valid
            UpdateProductRequest request
    ) {
        updateProductUseCase.updateProduct(
                id,
                new UpdateProductUseCase.UpdateProductCommand(
                        request.name(),
                        request.description(),
                        request.price()
                )
        );

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> products = getProductsUseCase.getProducts()
                .stream()
                .map(ProductResponse::from)
                .toList();

        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(
            @PathVariable
            Long id
    ) {
        ProductResponse response = ProductResponse.from(getProductUseCase.getProduct(id));

        return ResponseEntity.ok(response);
    }
}
