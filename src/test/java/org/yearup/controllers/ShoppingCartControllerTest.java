package org.yearup.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.service.ShoppingCartService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ShoppingCartController.class)
class ShoppingCartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ShoppingCartService shoppingCartService;

    @Test
    @WithMockUser(username = "testUser")
    void getCart_shouldReturnUsersCart() throws Exception {
        int userId = 1;
        ShoppingCart cart = new ShoppingCart();

        when(shoppingCartService.getUserId(any())).thenReturn(userId);
        when(shoppingCartService.getByUserId(userId)).thenReturn(cart);

        mockMvc.perform(get("/cart"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").exists());

        verify(shoppingCartService).getByUserId(userId);
    }

    @Test
    @WithMockUser(username = "testUser")
    void addItem_shouldAddProductToCart() throws Exception {
        int userId = 1;
        int productId = 15;
        ShoppingCart cart = new ShoppingCart();

        when(shoppingCartService.getUserId(any())).thenReturn(userId);
        when(shoppingCartService.addItem(productId, userId)).thenReturn(cart);

        mockMvc.perform(post("/cart/products/{productId}", productId))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.items").exists());

        verify(shoppingCartService).addItem(productId, userId);
    }

    @Test
    @WithMockUser(username = "testUser")
    void updateShoppingCart_shouldUpdateQuantity() throws Exception {
        int userId = 1;
        int productId = 15;
        ShoppingCart cart = new ShoppingCart();

        when(shoppingCartService.getUserId(any())).thenReturn(userId);
        when(shoppingCartService.updateItem(eq(userId), eq(productId), any(ShoppingCartItem.class)))
                .thenReturn(cart);

        mockMvc.perform(put("/cart/products/{productId}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "quantity": 3
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").exists());

        verify(shoppingCartService).updateItem(eq(userId), eq(productId), any(ShoppingCartItem.class));
    }

    @Test
    @WithMockUser(username = "testUser")
    void clearCart_shouldClearUsersCart() throws Exception {
        int userId = 1;
        ShoppingCart cart = new ShoppingCart();

        when(shoppingCartService.getUserId(any())).thenReturn(userId);
        when(shoppingCartService.clearCart(userId)).thenReturn(cart);

        mockMvc.perform(delete("/cart"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").exists());

        verify(shoppingCartService).clearCart(userId);
    }
}