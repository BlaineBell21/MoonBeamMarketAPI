package org.yearup.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.models.CartItem;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.repository.ShoppingCartRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceTest {
    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private ProductService productService;

    @Mock
    private UserService userService;

    @InjectMocks
    private ShoppingCartService shoppingCartService;

    @Test
    void getByUserId_shouldBuildCartFromCartItems() {
        int userId = 1;

        CartItem cartItem = new CartItem();
        cartItem.setUserId(userId);
        cartItem.setProductId(15);
        cartItem.setQuantity(2);

        Product product = new Product(
                15,
                "Test Product",
                new BigDecimal("19.99"),
                1,
                "description",
                "subcategory",
                10,
                false,
                "image.jpg"
        );

        when(shoppingCartRepository.findByUserId(userId))
                .thenReturn(List.of(cartItem));

        when(productService.getById(15))
                .thenReturn(product);

        ShoppingCart result = shoppingCartService.getByUserId(userId);

        assertEquals(1, result.getItems().size());

        ShoppingCartItem item = result.getItems().get(15);

        assertEquals(product, item.getProduct());
        assertEquals(2, item.getQuantity());
    }

    @Test
    void addItem_shouldCreateNewCartItem_whenProductIsNotInCart() {
        int userId = 1;
        int productId = 15;

        Product product = new Product(
                productId,
                "Test Product",
                new BigDecimal("19.99"),
                1,
                "description",
                "subcategory",
                10,
                false,
                "image.jpg"
        );

        when(shoppingCartRepository.findByUserIdAndProductId(userId, productId))
                .thenReturn(null);

        when(productService.getById(productId))
                .thenReturn(product);

        when(shoppingCartRepository.findByUserId(userId))
                .thenReturn(List.of());

        ShoppingCart result = shoppingCartService.addItem(productId, userId);

        ArgumentCaptor<CartItem> captor = ArgumentCaptor.forClass(CartItem.class);

        verify(shoppingCartRepository).save(captor.capture());

        CartItem savedItem = captor.getValue();

        assertEquals(userId, savedItem.getUserId());
        assertEquals(productId, savedItem.getProductId());
        assertEquals(1, savedItem.getQuantity());

        assertNotNull(result);
    }

    @Test
    void addItem_shouldIncreaseQuantity_whenProductAlreadyExistsInCart() {
        int userId = 1;
        int productId = 15;

        CartItem existingItem = new CartItem();
        existingItem.setUserId(userId);
        existingItem.setProductId(productId);
        existingItem.setQuantity(2);

        Product product = new Product(
                productId,
                "Test Product",
                new BigDecimal("19.99"),
                1,
                "description",
                "subcategory",
                10,
                false,
                "image.jpg"
        );

        when(shoppingCartRepository.findByUserIdAndProductId(userId, productId))
                .thenReturn(existingItem);

        when(productService.getById(productId))
                .thenReturn(product);

        when(shoppingCartRepository.findByUserId(userId))
                .thenReturn(List.of(existingItem));

        ShoppingCart result = shoppingCartService.addItem(productId, userId);

        assertEquals(3, existingItem.getQuantity());

        verify(shoppingCartRepository).save(existingItem);

        assertNotNull(result);
    }

    @Test
    void updateItem_shouldUpdateQuantity_whenQuantityIsValid() {
        int userId = 1;
        int productId = 15;

        CartItem existingItem = new CartItem();
        existingItem.setUserId(userId);
        existingItem.setProductId(productId);
        existingItem.setQuantity(1);

        ShoppingCartItem updatedItem = new ShoppingCartItem();
        updatedItem.setQuantity(4);

        Product product = new Product(
                productId,
                "Test Product",
                new BigDecimal("19.99"),
                1,
                "description",
                "subcategory",
                10,
                false,
                "image.jpg"
        );

        when(productService.getById(productId)).thenReturn(product);

        when(shoppingCartRepository.findByUserIdAndProductId(userId, productId))
                .thenReturn(existingItem);

        when(shoppingCartRepository.findByUserId(userId))
                .thenReturn(List.of(existingItem));

        ShoppingCart result = shoppingCartService.updateItem(userId, productId, updatedItem);

        assertEquals(4, existingItem.getQuantity());

        verify(shoppingCartRepository).save(existingItem);

        assertNotNull(result);
    }

    @Test
    void updateItem_shouldThrowBadRequest_whenQuantityIsGreaterThanStock() {
        int userId = 1;
        int productId = 15;

        CartItem existingItem = new CartItem();
        existingItem.setUserId(userId);
        existingItem.setProductId(productId);
        existingItem.setQuantity(1);

        ShoppingCartItem updatedItem = new ShoppingCartItem();
        updatedItem.setQuantity(20);

        Product product = new Product(
                productId,
                "Test Product",
                new BigDecimal("19.99"),
                1,
                "description",
                "subcategory",
                10,
                false,
                "image.jpg"
        );

        when(productService.getById(productId)).thenReturn(product);

        when(shoppingCartRepository.findByUserIdAndProductId(userId, productId))
                .thenReturn(existingItem);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> shoppingCartService.updateItem(userId, productId, updatedItem)
        );

        assertEquals(400, exception.getStatusCode().value());

        verify(shoppingCartRepository, never()).save(any(CartItem.class));
    }

    @Test
    void clearCart_shouldDeleteAllItemsForUserAndReturnEmptyCart() {
        int userId = 1;

        when(shoppingCartRepository.findByUserId(userId))
                .thenReturn(List.of());

        ShoppingCart result = shoppingCartService.clearCart(userId);

        verify(shoppingCartRepository).deleteByUserId(userId);

        assertTrue(result.getItems().isEmpty());
    }
}