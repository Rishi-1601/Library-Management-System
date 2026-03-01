package com.library.controller;

import com.library.model.Category;
import com.library.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class CategoryController {
    
    @Autowired
    private CategoryService categoryService;
    
    // GET all categories - accessible to everyone
    @GetMapping
    public ResponseEntity<?> getAllCategories() {
        try {
            List<Category> categories = categoryService.getAllCategories();
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error fetching categories: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }
    
    // GET category by ID - accessible to everyone
    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        try {
            Category category = categoryService.getCategoryById(id);
            return ResponseEntity.ok(category);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(404).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error fetching category: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }
    
    // GET category by name - accessible to everyone
    @GetMapping("/name/{name}")
    public ResponseEntity<?> getCategoryByName(@PathVariable String name) {
        try {
            Category category = categoryService.getCategoryByName(name);
            return ResponseEntity.ok(category);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(404).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error fetching category: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }
    
    // POST create category - ADMIN only
    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody Category category, HttpServletRequest request) {
        try {
            // Check user role from header
            String userRole = request.getHeader("X-User-Role");
            if (!"ADMIN".equals(userRole)) {
                return ResponseEntity.status(403).body("Only admins can create categories");
            }
            
            Category createdCategory = categoryService.createCategory(category);
            return ResponseEntity.ok(createdCategory);
            
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error creating category: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }
    
    // PUT update category - ADMIN only
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody Category category, HttpServletRequest request) {
        try {
            // Check user role from header
            String userRole = request.getHeader("X-User-Role");
            if (!"ADMIN".equals(userRole)) {
                return ResponseEntity.status(403).body("Only admins can update categories");
            }
            
            Category updatedCategory = categoryService.updateCategory(id, category);
            return ResponseEntity.ok(updatedCategory);
            
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error updating category: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }
    
    // DELETE category - ADMIN only
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id, HttpServletRequest request) {
        try {
            // Check user role from header
            String userRole = request.getHeader("X-User-Role");
            if (!"ADMIN".equals(userRole)) {
                return ResponseEntity.status(403).body("Only admins can delete categories");
            }
            
            categoryService.deleteCategory(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Category deleted successfully");
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error deleting category: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }
    
    // DELETE category by name - ADMIN only
    @DeleteMapping("/name/{name}")
    public ResponseEntity<?> deleteCategoryByName(@PathVariable String name, HttpServletRequest request) {
        try {
            // Check user role from header
            String userRole = request.getHeader("X-User-Role");
            if (!"ADMIN".equals(userRole)) {
                return ResponseEntity.status(403).body("Only admins can delete categories");
            }
            
            categoryService.deleteCategoryByName(name);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Category deleted successfully");
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error deleting category: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }
    
    // GET search categories - accessible to everyone
    @GetMapping("/search")
    public ResponseEntity<?> searchCategories(@RequestParam String keyword) {
        try {
            List<Category> categories = categoryService.searchCategories(keyword);
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error searching categories: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }
    
    // GET category statistics - accessible to everyone
    @GetMapping("/stats")
    public ResponseEntity<?> getCategoryStats() {
        try {
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalCategories", categoryService.getTotalCategoriesCount());
            stats.put("categories", categoryService.getAllCategories());
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error fetching category statistics: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }
    
    // POST create multiple categories - ADMIN only
    @PostMapping("/bulk")
    public ResponseEntity<?> createCategories(@RequestBody List<Category> categories, HttpServletRequest request) {
        try {
            // Check user role from header
            String userRole = request.getHeader("X-User-Role");
            if (!"ADMIN".equals(userRole)) {
                return ResponseEntity.status(403).body("Only admins can create categories");
            }
            
            List<Category> createdCategories = categoryService.createCategories(categories);
            return ResponseEntity.ok(createdCategories);
            
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error creating categories: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }
}