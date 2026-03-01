package com.library.controller;

import com.library.model.BorrowRecord;
import com.library.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/borrow")
@CrossOrigin(origins = "*")
public class BorrowController {

    @Autowired
    private BorrowService borrowService;

    // Borrow a book
    @PostMapping("/borrow")
    public ResponseEntity<?> borrowBook(@RequestParam Long userId, @RequestParam Long bookId) {
        try {
            BorrowRecord borrowRecord = borrowService.borrowBook(userId, bookId);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Book borrowed successfully");
            response.put("borrowRecord", borrowRecord);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // Return a book
    @PostMapping("/return/{borrowRecordId}")
    public ResponseEntity<?> returnBook(@PathVariable Long borrowRecordId) {
        try {
            BorrowRecord borrowRecord = borrowService.returnBook(borrowRecordId);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Book returned successfully");
            response.put("borrowRecord", borrowRecord);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // Get user's borrow records
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserBorrowRecords(@PathVariable Long userId) {
        try {
            List<BorrowRecord> records = borrowService.getUserBorrowRecords(userId);
            return ResponseEntity.ok(records);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // Get user's currently borrowed books
    @GetMapping("/user/{userId}/current")
    public ResponseEntity<?> getUserCurrentlyBorrowed(@PathVariable Long userId) {
        try {
            List<BorrowRecord> records = borrowService.getUserCurrentlyBorrowed(userId);
            return ResponseEntity.ok(records);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // Get all overdue books
    @GetMapping("/overdue")
    public ResponseEntity<?> getOverdueBooks() {
        try {
            List<BorrowRecord> records = borrowService.getOverdueBooks();
            return ResponseEntity.ok(records);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // Get borrow records for a specific book
    @GetMapping("/book/{bookId}")
    public ResponseEntity<?> getBookBorrowRecords(@PathVariable Long bookId) {
        try {
            List<BorrowRecord> records = borrowService.getBookBorrowRecords(bookId);
            return ResponseEntity.ok(records);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // Check if book is available
    @GetMapping("/check-availability/{bookId}")
    public ResponseEntity<?> checkBookAvailability(@PathVariable Long bookId) {
        try {
            boolean isAvailable = borrowService.isBookAvailable(bookId);
            int availableCopies = borrowService.getAvailableCopiesCount(bookId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("bookId", bookId);
            response.put("isAvailable", isAvailable);
            response.put("availableCopies", availableCopies);
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // Get borrow record by ID
    @GetMapping("/record/{recordId}")
    public ResponseEntity<?> getBorrowRecordById(@PathVariable Long recordId) {
        try {
            // You might want to add this method to BorrowService if needed
            // For now, let's return a not implemented message
            Map<String, String> response = new HashMap<>();
            response.put("message", "Get by ID functionality coming soon");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // Renew a borrowed book
    @PostMapping("/renew/{borrowRecordId}")
    public ResponseEntity<?> renewBook(@PathVariable Long borrowRecordId, @RequestParam(defaultValue = "7") int extraDays) {
        try {
            BorrowRecord borrowRecord = borrowService.renewBook(borrowRecordId, extraDays);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Book renewed successfully");
            response.put("borrowRecord", borrowRecord);
            response.put("newDueDate", borrowRecord.getDueDate());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // Get borrowing statistics for a user
    @GetMapping("/user/{userId}/stats")
    public ResponseEntity<?> getUserBorrowStats(@PathVariable Long userId) {
        try {
            List<BorrowRecord> allRecords = borrowService.getUserBorrowRecords(userId);
            List<BorrowRecord> currentRecords = borrowService.getUserCurrentlyBorrowed(userId);
            long totalBorrowed = borrowService.getTotalBorrowedCount(userId);
            
            Map<String, Object> stats = new HashMap<>();
            stats.put("userId", userId);
            stats.put("totalBorrowed", allRecords.size());
            stats.put("currentlyBorrowed", currentRecords.size());
            stats.put("totalBorrowedCount", totalBorrowed);
            stats.put("returnedCount", allRecords.size() - currentRecords.size());
            
            return ResponseEntity.ok(stats);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // Get all borrow records (admin only - you can add role check later)
    @GetMapping("/all")
    public ResponseEntity<?> getAllBorrowRecords() {
        try {
            // This method might need to be added to BorrowService
            // For now, return a message
            Map<String, String> response = new HashMap<>();
            response.put("message", "Get all records functionality coming soon");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}