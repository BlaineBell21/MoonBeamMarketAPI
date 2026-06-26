package org.yearup.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.models.*;

import org.yearup.repository.OrderRepository;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ShoppingCartService shoppingCartService;

    @Mock
    private ProfileService profileService;

    @Mock
    private ReceiptService receiptService;

    @Mock
    private Principal principal;

    @InjectMocks
    private OrderService orderService;

    @Test
    void calculateTotal_shouldAddItemsAndShipping() {
        OrderItem item1 = new OrderItem();
        item1.setPrice(new BigDecimal("10.00"));
        item1.setQuantity(2);

        OrderItem item2 = new OrderItem();
        item2.setPrice(new BigDecimal("157.57"));
        item2.setQuantity(2);

        OrderItem item3 = new OrderItem();
        item3.setPrice(new BigDecimal("19.99"));
        item3.setQuantity(2);

        List<OrderItem> itemList = new ArrayList<>();

        System.out.println(item1.getPrice());
        System.out.println(item2.getPrice());
        System.out.println(item3.getPrice());

        BigDecimal total = orderService.calculateTotal(itemList);

        assertEquals(new BigDecimal("381.11"), total);
    }

    @Test
    void checkout_shouldThrowBadRequest_whenCartIsEmpty() {
        int userId = 1;

        ShoppingCart emptyCart = new ShoppingCart();

        when(profileService.getUserId(principal)).thenReturn(userId);
        when(shoppingCartService.getByUserId(userId)).thenReturn(emptyCart);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> orderService.checkout(principal));

        assertEquals(400, exception.getStatusCode().value());

        verify(orderRepository, never()).save(any(Order.class));
        verify(receiptService, never()).saveReceipt(any(Order.class));
        verify(shoppingCartService, never()).clearCart(userId);
    }

    @Test
    void checkout_shouldCreateOrderSaveReceiptAndClearCart() {
        int userId = 1;

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

        ShoppingCartItem cartItem = new ShoppingCartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(2);

        ShoppingCart cart = new ShoppingCart();
        cart.add(cartItem);

        Profile profile = new Profile();
        profile.setAddress("123 Main St");
        profile.setCity("San Francisco");
        profile.setState("CA");
        profile.setZip("94105");

        when(profileService.getUserId(principal)).thenReturn(userId);
        when(shoppingCartService.getByUserId(userId)).thenReturn(cart);
        when(profileService.getProfileById(principal)).thenReturn(profile);

        Order result = orderService.checkout(principal);

        assertEquals(userId, result.getUserId());
        assertEquals("123 Main St", result.getAddress());
        assertEquals("San Francisco", result.getCity());
        assertEquals("CA", result.getState());
        assertEquals("94105", result.getZip());
        assertEquals(new BigDecimal("5.99"), result.getShippingAmount());
        assertEquals(new BigDecimal("45.97"), result.getTotal());
        assertEquals(1, result.getItems().size());

        OrderItem savedItem = result.getItems().get(0);

        assertEquals(15, savedItem.getProductId());
        assertEquals("Test Product", savedItem.getProductName());
        assertEquals(2, savedItem.getQuantity());
        assertEquals(new BigDecimal("19.99"), savedItem.getPrice());
        assertSame(result, savedItem.getOrder());

        verify(orderRepository, times(1)).save(result);
        verify(receiptService, times(1)).saveReceipt(result);
        verify(shoppingCartService, times(1)).clearCart(userId);
    }

    @Test
    void getCartItems_shouldConvertShoppingCartItemsToOrderItems() {
        int userId = 1;

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

        ShoppingCartItem cartItem = new ShoppingCartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(2);

        ShoppingCart cart = new ShoppingCart();
        cart.add(cartItem);

        when(shoppingCartService.getByUserId(userId)).thenReturn(cart);

        List<OrderItem> orderItems = orderService.getCartItems(userId);

        assertEquals(1, orderItems.size());

        OrderItem orderItem = orderItems.get(0);

        assertEquals(15, orderItem.getProductId());
        assertEquals("Test Product", orderItem.getProductName());
        assertEquals(2, orderItem.getQuantity());
        assertEquals(new BigDecimal("19.99"), orderItem.getPrice());
    }
}