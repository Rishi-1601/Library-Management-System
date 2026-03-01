package com.library.repository;

import com.library.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    // Find category by name (exact match)
    Optional<Category> findByName(String name);
    
    // Check if category exists by name
    boolean existsByName(String name);
    
    // Find categories containing keyword in name (case insensitive)
    List<Category> findByNameContainingIgnoreCase(String keyword);
    
    // Delete category by name
    void deleteByName(String name);
}