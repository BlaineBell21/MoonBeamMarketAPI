package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.models.OrderItem;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.repository.OrderItemRepository;

import java.util.List;

@Service
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final ProductService productService;


    public OrderItemService(OrderItemRepository orderItemRepository, ProductService productService) {
        this.orderItemRepository = orderItemRepository;
        this.productService = productService;
    }
    public List<OrderItem> getItems(){
        return orderItemRepository.findAll();
    }


    public OrderItem createOrderItem(ShoppingCartItem item){
        OrderItem newItem = new OrderItem();

        newItem.setProductId(item.getProductId());
        newItem.setProductName(item.getProduct().getName());
        newItem.setPrice(item.getProduct().getPrice());
        newItem.setQuantity(item.getQuantity());

        return orderItemRepository.save(newItem);
    }
}
