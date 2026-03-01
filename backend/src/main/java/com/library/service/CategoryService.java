package com.library.service;

import com.library.model.Category;
import com.library.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryService {
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    // Create new category
    public Category createCategory(Category category) {
        // Validate category name
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            throw new RuntimeException("Category name is required");
        }
        
        // Check if category already exists
        if (categoryRepository.existsByName(category.getName())) {
            throw new RuntimeException("Category with name '" + category.getName() + "' already exists");
        }
        
        return categoryRepository.save(category);
    }
    
    // Get all categories
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
    
    // Get category by ID
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
    }
    
    // Get category by name
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name)
            .orElseThrow(() -> new RuntimeException("Category not found with name: " + name));
    }
    
    // Update category
    public Category updateCategory(Long id, Category categoryDetails) {
        Category category = getCategoryById(id);
        
        // Update name if provided and not empty
        if (categoryDetails.getName() != null && !categoryDetails.getName().trim().isEmpty()) {
            // Check if new name is already taken by another category
            if (!category.getName().equals(categoryDetails.getName()) && 
                categoryRepository.existsByName(categoryDetails.getName())) {
                throw new RuntimeException("Category with name '" + categoryDetails.getName() + "' already exists");
            }
            category.setName(categoryDetails.getName());
        }
        
        // Update description if provided
        if (categoryDetails.getDescription() != null) {
            category.setDescription(categoryDetails.getDescription());
        }
        
        return categoryRepository.save(category);
    }
    
    // Delete category
    public void deleteCategory(Long id) {
        Category category = getCategoryById(id);
        
        // Check if category has books
        if (category.getBooks() != null && !category.getBooks().isEmpty()) {
            throw new RuntimeException("Cannot delete category with existing books");
        }
        
        categoryRepository.delete(category);
    }
    
    // Delete category by name
    public void deleteCategoryByName(String name) {
        Category category = getCategoryByName(name);
        
        // Check if category has books
        if (category.getBooks() != null && !category.getBooks().isEmpty()) {
            throw new RuntimeException("Cannot delete category with existing books");
        }
        
        categoryRepository.deleteByName(name);
    }
    
    // Search categories by keyword
    public List<Category> searchCategories(String keyword) {
        return categoryRepository.findByNameContainingIgnoreCase(keyword);
    }
    
    // Get total count of categories
    public long getTotalCategoriesCount() {
        return categoryRepository.count();
    }
    
    // Check if category exists
    public boolean categoryExists(Long id) {
        return categoryRepository.existsById(id);
    }
    
    public boolean categoryExistsByName(String name) {
        return categoryRepository.existsByName(name);
    }
    
    // Create multiple categories at once
    public List<Category> createCategories(List<Category> categories) {
        for (Category category : categories) {
            if (categoryRepository.existsByName(category.getName())) {
                throw new RuntimeException("Category '" + category.getName() + "' already exists");
            }
        }
        return categoryRepository.saveAll(categories);
    }
}