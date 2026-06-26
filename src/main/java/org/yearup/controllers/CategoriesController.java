package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.models.Category;
import org.yearup.models.Product;
import org.yearup.service.CategoryService;
import org.yearup.service.ProductService;

import java.util.List;


@RestController
@RequestMapping("categories")
@CrossOrigin
public class CategoriesController
{
    private final CategoryService categoryService;
    private final ProductService productService;

    @Autowired
    CategoriesController(CategoryService categoryService, ProductService productService){
        this.categoryService = categoryService;
        this.productService = productService;
    }

    @GetMapping
    @PreAuthorize("permitAll()")
    // allows any user to see all product categories
    public ResponseEntity<List<Category>> getAll() {
        List<Category> categories = categoryService.getAllCategories();
           return ResponseEntity.ok(categories);
    }


    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    // allows any user to view a specific product category
    public ResponseEntity<Category> getByCategoryId(@PathVariable Integer id){
        Category category = categoryService.getByCategoryId(id);
        int cat = category.getCategoryId();
        // checks if category exists
        categoryValidation(cat);

        return ResponseEntity.ok(category);
    }

    @GetMapping("{categoryId}/products")
    @PreAuthorize("permitAll()")
    // allows any user to products based on product category
    public ResponseEntity<List<Product>> getProductsByCategoryId(@PathVariable Integer categoryId) {
        List<Product> product = productService.getAllProducts();
        // checks if category exists
        Category category = categoryService.getByCategoryId(categoryId);
        int cat = category.getCategoryId();
        // checks if category exists
        categoryValidation(cat);

        return  ResponseEntity.ok(product);
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    // allows only admin to add new categories
    public ResponseEntity<Category> addCategory(@RequestBody Category category) {
        Category saved = categoryService.createCategory(category);

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    // allows only admin to update a category
    public ResponseEntity<Category> updateCategory(@PathVariable int id, @RequestBody Category category) {
        int cat = category.getCategoryId();
        // checks if category exists
        categoryValidation(cat);

        Category updated = categoryService.update(id, category);

        return ResponseEntity.ok(updated);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    // allows only admin to delete a category
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {
        Category category = categoryService.getByCategoryId(id);
        int cat = category.getCategoryId();
        // checks if cat exists
        categoryValidation(cat);

       categoryService.delete(id);
       return ResponseEntity.noContent().build();
    }

    public static void categoryValidation(Integer categoryId){
        if (categoryId == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Category not found.\n" +
                            "This category either isn't available or the category Id was entered in incorrectly.");
        }
    }
}
