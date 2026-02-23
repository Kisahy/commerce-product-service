package com.kisahy.commerce.product.adapter.out.persistence;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.kisahy.commerce.product.domain.model.Product;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@Import(ProductPersistenceAdapter.class)
public class ProductPersistenceAdapterTest {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @Autowired
    private ProductPersistenceAdapter productPersistenceAdapter;
    
    @Test
    @DisplayName("save: 신규 상품 저장 시 ID가 할당된 상품을 반환한다")
    void save_newProduct_assignsId() {
        Product saved = productPersistenceAdapter.save(
                Product.create("상품A", "설명", 10000.0, 100)
        );

        Product updated = new Product(
                saved.getId(), "수정된 상품명", "수정된 설명", 20000.0,
                saved.getStockQuantity(), saved.getStatus(),
                saved.getCreatedAt(), saved.getUpdatedAt()
        );
        Product result = productPersistenceAdapter.save(updated);

        assertThat(result.getId()).isEqualTo(saved.getId());
        assertThat(result.getName()).isEqualTo("수정된 상품명");
        assertThat(result.getPrice()).isEqualTo(20000.0);
    }

    @Test
    @DisplayName("deleteById: 상품 삭제 후 조회 시 Optional.empty()를 반환한다")
    void deleteById_removesProduct() {
        Product saved = productPersistenceAdapter.save(
                Product.create("상품E", "설명", 10000.0, 100)
        );

        productPersistenceAdapter.deleteById(saved.getId());

        assertThat(productPersistenceAdapter.loadById(saved.getId())).isEmpty();
    }

    @Test
    @DisplayName("loadById: 저장된 상품을 ID로 조회하면 Optional에 담아 반환한다")
    void loadById_existingProduct_returnsProduct() {
        Product saved = productPersistenceAdapter.save(
                Product.create("상품B", "설명", 10000.0, 100)
        );

        Optional<Product> found = productPersistenceAdapter.loadById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("상품B");
    }

    @Test
    @DisplayName("loadById: 존재하지 않는 ID 조회 시 Optional.empty()를 반환한다")
    void loadById_nonExistentId_returnsEmpty() {
        Optional<Product> result = productPersistenceAdapter.loadById(999L);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("loadAll: 저장된 모든 상품 목록을 반환한다")
    void loadAll_returnsAllProducts() {
        productPersistenceAdapter.save(Product.create("상품C", "설명", 10000.0, 100));
        productPersistenceAdapter.save(Product.create("상품D", "설명", 10000.0, 100));

        List<Product> products = productPersistenceAdapter.loadAll();

        assertThat(products).hasSizeGreaterThanOrEqualTo(2);
    }
}
