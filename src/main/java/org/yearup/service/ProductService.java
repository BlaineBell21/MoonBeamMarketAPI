package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.models.Product;
import org.yearup.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> search(Integer categoryId, BigDecimal minPrice, BigDecimal maxPrice, String subCategory) {
        // search method that allows user to search through any available filter categories
        List<Product> products = categoryId != null ? productRepository.findByCategoryId(categoryId) : productRepository.findAll();

        return products.stream()
                // checks whether properties are null and filters products
                       .filter(p -> minPrice == null || p.getPrice().compareTo(minPrice) >= 0)
                       .filter(p -> maxPrice == null || p.getPrice().compareTo(maxPrice) <= 0)
                       .filter(p -> subCategory == null || subCategory.equalsIgnoreCase(p.getSubCategory()))
                        .toList();
    }

    public Product getById(Integer productId) {
        return productRepository.findById(productId).orElse(null);
    }

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public Product create(Product product) {
        return productRepository.save(product);
    }

    public Product update(int productId, Product product) {
        // updates a product's values

        // checks if product exists before attempting to update
        Product existing = productRepository.findById(productId).orElseThrow();
        existing.setName(product.getName());
        existing.setPrice(product.getPrice());
        existing.setCategoryId(product.getCategoryId());
        existing.setDescription(product.getDescription());
        existing.setSubCategory(product.getSubCategory());
        existing.setStock(product.getStock());
        existing.setFeatured(product.isFeatured());
        existing.setImageUrl(product.getImageUrl());

        // saves updated product to data base
        return productRepository.save(existing);
    }

    public void delete(int productId) {
        productRepository.deleteById(productId);
    }
}
