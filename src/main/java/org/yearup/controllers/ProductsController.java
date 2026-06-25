package org.yearup.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.models.Product;
import org.yearup.service.ProductService;
import org.yearup.utils.ValidationCheck;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("products")
@CrossOrigin
public class ProductsController
{
    private final ProductService productService;

    public ProductsController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @PreAuthorize("permitAll()")
    // allows any user to use search features
    public List<Product> allProducts(@RequestParam(name="cat", required = false) Integer categoryId,
                                @RequestParam(name="minPrice", required = false) BigDecimal minPrice,
                                @RequestParam(name="maxPrice", required = false) BigDecimal maxPrice,
                                @RequestParam(name="subCategory", required = false) String subCategory) {
        return productService.search(categoryId, minPrice, maxPrice, subCategory);
    }


    @GetMapping("/{productId}")
    @PreAuthorize("permitAll()")
    // allows any user to get information of a specific product
    public Product getById(@PathVariable int productId) {
        Product product = productService.getById(productId);

        // checks if product exists before attempting to retrieve it
       ValidationCheck.productValidation(product);

        return product;
    }


    @PostMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    // allows only admin to add new products
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        Product saved = productService.create(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{productId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    // allows only admin to update products
    public Product updateProduct(@PathVariable int productId, @RequestBody Product product) {
        // checks if product exists before attempting to update it
        ValidationCheck.productValidation(product);

        return productService.update(productId, product);
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    // allows only admin to delete product
    public ResponseEntity<Void> deleteProduct(@PathVariable int productId) {
        Product product = productService.getById(productId);

        // checks if product exists before trying to delete
        ValidationCheck.productValidation(product);

        productService.delete(productId);

        return ResponseEntity.ok().build();
    }
}
