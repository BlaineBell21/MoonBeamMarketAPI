package org.yearup.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.models.Order;
import org.yearup.service.OrderService;

import java.security.Principal;

@RestController
@RequestMapping("orders")
@CrossOrigin
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @PostMapping()
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Order> finishOrder(Principal principal){
        int userId = orderService.getUserId(principal);

        Order checkoutOrder = orderService.checkout(principal);

        return ResponseEntity.status(HttpStatus.CREATED).body(checkoutOrder);
   }
}
