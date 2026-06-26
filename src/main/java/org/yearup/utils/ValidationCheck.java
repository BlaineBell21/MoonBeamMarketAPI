package org.yearup.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.models.Product;

public class ValidationCheck {
    public static void productValidation(Product product){
        // validates if a product exists in the database
        if(product == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Product not found.\n" +
                            "This product either isn't available or the Product Id was entered in incorrectly.");
        }
    }
}
