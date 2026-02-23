package com.kisahy.commerce.product.adapter.in.web;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kisahy.commerce.product.application.port.in.CreateProductUseCase;
import com.kisahy.commerce.product.application.port.in.DeleteProductUseCase;
import com.kisahy.commerce.product.application.port.in.GetProductUseCase;
import com.kisahy.commerce.product.application.port.in.GetProductsUseCase;
import com.kisahy.commerce.product.application.port.in.UpdateProductUseCase;
import com.kisahy.commerce.product.domain.exception.ProductNotFoundException;
import com.kisahy.commerce.product.domain.model.Product;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateProductUseCase createProductUseCase;
    @MockitoBean
    private UpdateProductUseCase updateProductUseCase;
    @MockitoBean
    private DeleteProductUseCase deleteProductUseCase;
    @MockitoBean
    private GetProductUseCase getProductUseCase;
    @MockitoBean
    private GetProductsUseCase getProductsUseCase;

    private Product sampleProduct() {
        return new Product(
                1L,
                "테스트 상품",
                "설명",
                1000.0,
                100,
                1,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @Test
    @DisplayName("POST /products: 상품 생성 성공 시 201 Created와 Location 헤더 반환")
    void createProduct_returns201() throws Exception {
        given(createProductUseCase.createProduct(any())).willReturn(1L);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new CreateProductRequest(
                                        "테스트 상품",
                                        "설명",
                                        1000.0,
                                        100
                                )
                        )))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/products/1"));
    }

    @Test
    @DisplayName("POST /products: 유효성 검사 실패 시 400 Bad Request 반환")
    void createProduct_validationFail_returns400() throws Exception {
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new CreateProductRequest(
                                        "",
                                        "설명",
                                        10000.0,
                                        100
                                )
                        )))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_FAILED"));
    }

    @Test
    @DisplayName("PUT /products/{id}: 상품 수정 성공 시 204 No Content 반환")
    void updateProduct_returns204() throws Exception {
        willDoNothing().given(updateProductUseCase).updateProduct(eq(1L), any());

        mockMvc.perform(put("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new UpdateProductRequest("수정명", "수정설명", 20000.0)
                        )))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("PUT /products/{id}: 존재하지 않는 상품 수정 시 404 반환")
    void updateProduct_notFound_returns404() throws Exception {
        willThrow(new ProductNotFoundException(999L))
                .given(updateProductUseCase).updateProduct(eq(999L), any());

        mockMvc.perform(put("/products/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new UpdateProductRequest("수정명", "수정설명", 20000.0)
                        )))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("PRODUCT_NOT_FOUND"));
    }

    @Test
    @DisplayName("DELETE /products/{id}: 상품 삭제 성공 시 204 No Content 반환")
    void deleteProduct_returns204() throws Exception {
        willDoNothing().given(deleteProductUseCase).deleteProduct(1L);

        mockMvc.perform(delete("/products/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /products/{id}: 존재하지 않는 상품 삭제 시 404 반환")
    void deleteProduct_notFound_returns404() throws Exception {
        willThrow(new ProductNotFoundException(999L))
                .given(deleteProductUseCase).deleteProduct(999L);

        mockMvc.perform(delete("/products/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("PRODUCT_NOT_FOUND"));
    }

    @Test
    @DisplayName("GET /products/{id}: 존재하는 ID 조회 시 200 OK 반환")
    void getProduct_found_returns200() throws Exception {
        given(getProductUseCase.getProduct(1L)).willReturn(sampleProduct());

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("GET /products/{id}: 존재하지 않는 ID 조회 시 404 Not Found 반환")
    void getProduct_notFound_returns404() throws Exception {
        given(getProductUseCase.getProduct(999L))
                .willThrow(new ProductNotFoundException(999L));

        mockMvc.perform(get("/products/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("PRODUCT_NOT_FOUND"));
    }

    @Test
    @DisplayName("GET /products: 상품 목록 조회 성공 시 200 OK와 리스트 반환")
    void getProducts_returns200() throws Exception {
        given(getProductsUseCase.getProducts()).willReturn(List.of(sampleProduct()));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("테스트 상품"));
    }
}
