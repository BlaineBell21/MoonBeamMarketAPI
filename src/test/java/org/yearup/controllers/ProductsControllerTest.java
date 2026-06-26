package org.yearup.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.yearup.models.Product;
import org.yearup.service.ProductService;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductsController.class)
class ProductsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void addProduct_shouldCreateProduct() throws Exception {
        Product savedProduct = new Product(
                1, "name 1", new BigDecimal("10.99"), 1,
                "description", "subcategory", 20,
                true, "url.jpg"
        );
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteProduct_shouldDeleteProduct() throws Exception {
        int productId = 1;

        Product product = new Product(
                productId, "name 1", new BigDecimal("10.99"), 1,
                "description", "subcategory", 20,
                true, "url.jpg"
        );

        when(productService.getById(productId)).thenReturn(product);
        doNothing().when(productService).delete(productId);

        mockMvc.perform(delete("/products/{productId}", productId))
                .andExpect(status().isNoContent());

        verify(productService).delete(productId);
    }
    @Test
    void getById_shouldReturnProduct() throws Exception {
        Product product = new Product(
                1, "name 1", new BigDecimal("10.99"), 1,
                "description", "subcategory", 20,
                true, "url.jpg"
        );

        when(productService.getById(1)).thenReturn(product);

        mockMvc.perform(get("/products/{productId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(1))
                .andExpect(jsonPath("$.name").value("name 1"));
    }
}