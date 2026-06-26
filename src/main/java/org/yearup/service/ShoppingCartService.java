package org.yearup.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.models.*;
import org.yearup.repository.ShoppingCartRepository;
import org.yearup.utils.ValidationCheck;

import java.security.Principal;
import java.util.List;

@Service
public class ShoppingCartService
{
    // a shopping cart is built from cart rows plus a product lookup for each row
    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductService productService;
    private final UserService userService;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository, ProductService productService, UserService userService) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.productService = productService;
        this.userService = userService;
    }

    public ShoppingCart getByUserId(int userId) {
        ShoppingCart cart = new ShoppingCart();

        List<CartItem> cartItems = shoppingCartRepository.findByUserId(userId);

        for (CartItem cartItem : cartItems){
           Product product = productService.getById(cartItem.getProductId());

           ShoppingCartItem item = new ShoppingCartItem();

           item.setProduct(product);
           item.setQuantity(cartItem.getQuantity());

           cart.add(item);
        }
        return cart;
    }

    public int getUserId(Principal principal){
        String userName = principal.getName();
        User user = userService.getByUserName(userName);
        return user.getId();
    }

    public ShoppingCart addItem(int productId, int userId){

        CartItem existingCartItem = shoppingCartRepository.findByUserIdAndProductId(userId, productId);

        Product product = productService.getById(productId);

        ValidationCheck.productValidation(product); // Checks if product exists

       if (existingCartItem != null){
           int updatedQuantity = existingCartItem.getQuantity() + 1;
           existingCartItem.setQuantity(updatedQuantity);
           shoppingCartRepository.save(existingCartItem);
       } else {
           CartItem newCartItem = new CartItem();
           newCartItem.setProductId(productId);
           newCartItem.setUserId(userId);
           newCartItem.setQuantity(1);
           shoppingCartRepository.save(newCartItem);
       }
        return getByUserId(userId);
    }

    public ShoppingCart updateItem(int userId, int productId, ShoppingCartItem item){
        Product product = productService.getById(productId);
        ValidationCheck.productValidation(product); // Checks if product exists

       CartItem existingCartItem = shoppingCartRepository.findByUserIdAndProductId(userId, productId);


       existingCartItem.setQuantity(item.getQuantity());

       quantityCheck(existingCartItem, product);
       // Checks if inputted quantity is higher than 0 but less than maximum stock
        shoppingCartRepository.save(existingCartItem);
       return getByUserId(userId);
    }

    @Transactional
    public ShoppingCart clearCart(int userId){
        shoppingCartRepository.deleteByUserId(userId);
        return getByUserId(userId);
    }
    public static void quantityCheck(CartItem cartItem, Product product){
        // validates that the quantity being asked for isn't more than stock and isn't less than 1
        if (cartItem.getQuantity() < 1 || product.getStock() < cartItem.getQuantity()){
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Illegal item quantity.\n" +
                            "Inputted item quantity must be greater than 0 and less than maximum stock amount.");
        }
    }

}
