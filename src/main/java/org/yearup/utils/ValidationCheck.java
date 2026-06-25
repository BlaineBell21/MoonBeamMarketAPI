package org.yearup.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.models.CartItem;
import org.yearup.models.Product;
import org.yearup.models.Profile;

public class ValidationCheck {
    public static void productValidation(Product product){
        // validates if a product exists in the database
        if(product == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Product not found.\n" +
                            "This product either isn't available or the Product Id was entered in incorrectly.");
        }
    }

    public static void quantityCheck(CartItem cartItem, Product product){
        // validates that the quantity being asked for isn't more than stock and isn't less than 1
        if (cartItem.getQuantity() < 1 || product.getStock() < cartItem.getQuantity()){
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Illegal item quantity.\n" +
                            "Inputted item quantity must be greater than 0 and less than maximum stock amount.");
        }
    }

    public static void userValidation(Profile profile){
        // verifies that the user exists in database
        if (profile == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Profile not found.\n" +
                            "This user either doesn't exist or the incorrect ID was inputted.");
        }
    }
}
