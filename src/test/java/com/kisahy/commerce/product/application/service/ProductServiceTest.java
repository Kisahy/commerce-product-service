package com.kisahy.commerce.product.application.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kisahy.commerce.product.application.port.in.CreateProductUseCase;
import com.kisahy.commerce.product.application.port.in.UpdateProductUseCase;
import com.kisahy.commerce.product.application.port.out.DeleteProductPort;
import com.kisahy.commerce.product.application.port.out.LoadProductPort;
import com.kisahy.commerce.product.application.port.out.SaveProductPort;
import com.kisahy.commerce.product.domain.exception.ProductNotFoundException;
import com.kisahy.commerce.product.domain.model.Product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private SaveProductPort saveProductPort;
    @Mock
    private DeleteProductPort deleteProductPort;
    @Mock
    private LoadProductPort loadProductPort;

    @InjectMocks
    private ProductService productService;

    private Product sampleProduct;

    @BeforeEach
    void setUp() {
        sampleProduct = new Product(
                1L,
                "테스트 상품",
                "설명",
                100000.0,
                100,
                1,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @Test
    @DisplayName("상품 생성 성공: 저장된 상품 ID를 반환한다")
    void createProduct_success() {
        given(saveProductPort.save(any(Product.class))).willReturn(sampleProduct);

        Long id = productService.createProduct(
                new CreateProductUseCase.CreateProductCommand(
                        "테스트 상품",
                        "설명",
                        10000.0,
                        100
                )
        );

        assertThat(id).isEqualTo(1L);
        then(saveProductPort).should(times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("상품 수정 성공: 존재하는 상품을 수정하고 저장한다")
    void updateProduct_success() {
        given(loadProductPort.loadById(1L)).willReturn(Optional.of(sampleProduct));
        given(saveProductPort.save(any(Product.class))).willReturn(sampleProduct);

        productService.updateProduct(
                1L,
                new UpdateProductUseCase.UpdateProductCommand("변경명", "변경설명", 20000.0)
        );

        then(saveProductPort).should(times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("상품 수정 실패: 존재하지 않는 상품 수정 시 ProductNotFoundException 발생")
    void updateProduct_notFound() {
        given(loadProductPort.loadById(999L)).willReturn(Optional.empty());

        assertThatThrownBy(() ->
                productService.updateProduct(
                        999L,
                        new UpdateProductUseCase.UpdateProductCommand("변경명", "변경설명", 20000.0)
                )
        ).isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    @DisplayName("상품 삭제 성공: 존재하는 상품을 삭제한다")
    void deleteProduct_success() {
        given(loadProductPort.loadById(1L)).willReturn(Optional.of(sampleProduct));

        productService.deleteProduct(1L);

        then(deleteProductPort).should(times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("상품 삭제 실패: 존재하지 않는 상품 삭제 시 ProductNotFoundException 발생")
    void deleteProduct_notFound() {
        given(loadProductPort.loadById(999L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> productService.deleteProduct(999L))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    @DisplayName("단일 상품 조회 성공: 존재하는 ID로 조회하면 상품을 반환한다")
    void getProduct_found() {
        given(loadProductPort.loadById(1L)).willReturn(Optional.of(sampleProduct));

        Product result = productService.getProduct(1L);

        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("단일 상품 조회 실패: 존재하지 않는 ID 조회 시 ProductNotFoundException 발생")
    void getProduct_notFound() {
        given(loadProductPort.loadById(999L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> productService.getProduct(999L))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining("999");
    }

    @Test
    @DisplayName("상품 목록 조회: 전체 상품 리스트를 반환한다")
    void getProducts_returnsAll() {
        given(loadProductPort.loadAll()).willReturn(List.of(sampleProduct));

        List<Product> products = productService.getProducts();

        assertThat(products).hasSize(1);
        assertThat(products.getFirst().getId()).isEqualTo(1L);
    }
}
