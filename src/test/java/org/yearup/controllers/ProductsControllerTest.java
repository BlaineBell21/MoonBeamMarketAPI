package org.yearup.controllers;

import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.yearup.models.Product;
import org.yearup.service.ProductService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductsController.class)
class ProductsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;



    @Test
    void allProducts_shouldReturnListOfAllProducts() throws Exception{
        List<Product> products = getProducts();

        when(productService.getAllProducts()).thenReturn(products);
        
        mockMvc.perform(MockMvcRequestBuilders.get("/products").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("name 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description").value("a test product 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].stock").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].imageUrl").value("url 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].isFeature").value(false));
    }

    private static @NonNull List<Product> getProducts() {
        Product product1 = new Product(
                1,
                "name 1",
                10.99,
                1,
                "a test product 1",
                "a test sub category 1",
                20,
                true,
                "url 1" );

        Product product2 = new Product(
                2,
                "name 2",
                9.99,
                1,
                "a test product 2",
                "a test sub category 2",
                10,
                false,
                "url 2" );

        return Arrays.asList(product1, product2);
    }

    @Test
    void getById() throws Exception {
        Product product1 = new Product(
                1,
                "name 1",
                10.99,
                1,
                "a test product 1",
                "a test sub category 1",
                20,
                true,
                "url 1" );

        when(productService.getById(1)).thenReturn(product1);

        mockMvc.perform(MockMvcRequestBuilders.get("products/{id}", 1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())  // 200 OK
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("name 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(10.99))
                .andExpect(MockMvcResultMatchers.jsonPath("$.categoryId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("a test product 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.subCategory").value("a test sub category 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stock").value(20));
    }

    @Test
    void addProduct() {
    }

    @Test
    void updateProduct() {
    }

    @Test
    void deleteProduct() {
    }
}