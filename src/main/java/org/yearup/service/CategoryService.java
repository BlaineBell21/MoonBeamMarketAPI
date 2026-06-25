package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.models.Category;
import org.yearup.repository.CategoryRepository;

import java.util.List;

@Service
public class CategoryService
{
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        // gets all product categories
        return categoryRepository.findAll();
    }

    public Category getByCategoryId(int categoryId) {
        // gets individual product categories
        return categoryRepository.findById(categoryId).orElse(null);
    }


    public Category createCategory(Category category) {
        // creates a new product category
        return categoryRepository.save(category);
    }

    public Category update(int categoryId, Category updatedCategory) {
        // updates a product category
        Category existingCategory = getByCategoryId(categoryId);
        existingCategory.setName(updatedCategory.getName());
        existingCategory.setDescription(updatedCategory.getDescription());
        return categoryRepository.save(existingCategory);
    }

    public void delete(int categoryId) {
        // deletes a product category
        categoryRepository.deleteById(categoryId);
    }
}
