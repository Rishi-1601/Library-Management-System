package com.library.service;

import com.library.model.User;
import com.library.model.UserRole;
import com.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Register new user
    public User registerUser(User user) {
        // Check if username exists - FIXED: getUsername() not getUserName()
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists!");
        }
        
        // Check if email exists
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists!");
        }
        
        // Set default role if not set
        if (user.getRole() == null) {
            user.setRole(UserRole.MEMBER);
        }
        
        // Save and return user
        return userRepository.save(user);
    }

    // Find by username
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Find by email
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get user by ID
    public User getUserById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    // Update user
    public User updateUser(Long id, User userDetails) {
        User user = getUserById(id);
        
        user.setFullName(userDetails.getFullName());
        user.setPhone(userDetails.getPhone());      // ← NOW WORKS!
        user.setAddress(userDetails.getAddress());  // ← NOW WORKS!
        
        return userRepository.save(user);
    }

    // Delete user
    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }

    // Toggle user status
    public User toggleUserStatus(Long id) {
        User user = getUserById(id);
        user.setStatus(!user.isStatus());  // ← NOW WORKS!
        return userRepository.save(user);
    }

    // Change password
    public void changePassword(Long id, String oldPassword, String newPassword) {
        User user = getUserById(id);
        
        if (!user.getPassword().equals(oldPassword)) {
            throw new RuntimeException("Old password is incorrect!");
        }
        
        user.setPassword(newPassword);
        userRepository.save(user);
    }
}