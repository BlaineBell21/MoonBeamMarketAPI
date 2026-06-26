package org.yearup.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.models.*;
import org.yearup.service.ShoppingCartService;

import java.security.Principal;


@RestController
@RequestMapping("cart")
@CrossOrigin
public class ShoppingCartController {
    // a shopping cart controller depends on the service layer
    private final ShoppingCartService shoppingCartService;


    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    // each method in this controller requires a Principal object as a parameter
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ShoppingCart getCart(Principal principal) {
        // get the currently logged in username
        int userId = shoppingCartService.getUserId(principal);

        // use the shoppingCartService to get all items in the cart and return the cart
        return shoppingCartService.getByUserId(userId);
    }


    @PostMapping("/products/{productId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ShoppingCart> addItem(@PathVariable int productId, Principal principal){
        int userId = shoppingCartService.getUserId(principal);

        ShoppingCart updatedCart = shoppingCartService.addItem(productId, userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(updatedCart);
    }

    @PutMapping(value = "/products/{productId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ShoppingCart> updateShoppingCart(Principal principal, @PathVariable int productId, @RequestBody ShoppingCartItem item){
        int userId = shoppingCartService.getUserId(principal);

        ShoppingCart updatedItems = shoppingCartService.updateItem(userId, productId, item);

        return ResponseEntity.ok(updatedItems);
    }

    @DeleteMapping()
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ShoppingCart> clearCart(Principal principal){
        int userId = shoppingCartService.getUserId(principal);

        return ResponseEntity.ok(shoppingCartService.clearCart(userId));
    }
}
