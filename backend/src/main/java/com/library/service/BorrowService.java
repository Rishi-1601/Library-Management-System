package com.library.service;

import com.library.model.Book;
import com.library.model.BorrowRecord;
import com.library.model.BorrowStatus;
import com.library.model.User;
import com.library.repository.BookRepository;
import com.library.repository.BorrowRecordRepository;
import com.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class BorrowService {

    @Autowired
    private BorrowRecordRepository borrowRecordRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public BorrowRecord borrowBook(Long userId, Long bookId) {
        // Get user and book
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new RuntimeException("Book not found with id: " + bookId));

        // Check if book is available
        if (book.getAvailable() <= 0) {
            throw new RuntimeException("Book is not available for borrowing");
        }

        // Check if user already has this book borrowed
        Optional<BorrowRecord> existingBorrow = borrowRecordRepository
            .findByUserIdAndBookIdAndStatus(userId, bookId, BorrowStatus.BORROWED);
        
        if (existingBorrow.isPresent()) {
            throw new RuntimeException("You have already borrowed this book");
        }

        // Calculate due date (14 days from now)
        Date borrowDate = new Date();
        Date dueDate = new Date(borrowDate.getTime() + TimeUnit.DAYS.toMillis(14));

        // Create borrow record
        BorrowRecord borrowRecord = new BorrowRecord();
        borrowRecord.setUser(user);
        borrowRecord.setBook(book);
        borrowRecord.setBorrowDate(borrowDate);
        borrowRecord.setDueDate(dueDate);
        borrowRecord.setStatus(BorrowStatus.BORROWED);
        borrowRecord.setFine(0.0);

        // Update book availability
        book.setAvailable(book.getAvailable() - 1);
        bookRepository.save(book);

        return borrowRecordRepository.save(borrowRecord);
    }

    @Transactional
    public BorrowRecord returnBook(Long borrowRecordId) {
        BorrowRecord borrowRecord = borrowRecordRepository.findById(borrowRecordId)
            .orElseThrow(() -> new RuntimeException("Borrow record not found with id: " + borrowRecordId));

        if (borrowRecord.getStatus() == BorrowStatus.RETURNED) {
            throw new RuntimeException("Book already returned");
        }

        // Set return date
        Date returnDate = new Date();
        borrowRecord.setReturnDate(returnDate);
        borrowRecord.setStatus(BorrowStatus.RETURNED);

        // Calculate fine if overdue
        if (returnDate.after(borrowRecord.getDueDate())) {
            long diffInMillies = returnDate.getTime() - borrowRecord.getDueDate().getTime();
            long daysOverdue = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            
            // Fine of ₹10 per day
            double fine = daysOverdue * 10;
            borrowRecord.setFine(fine);
        }

        // Update book availability
        Book book = borrowRecord.getBook();
        book.setAvailable(book.getAvailable() + 1);
        bookRepository.save(book);

        return borrowRecordRepository.save(borrowRecord);
    }

    public List<BorrowRecord> getUserBorrowRecords(Long userId) {
        return borrowRecordRepository.findByUserId(userId);
    }

    public List<BorrowRecord> getUserCurrentlyBorrowed(Long userId) {
        return borrowRecordRepository.findByUserIdAndStatus(userId, BorrowStatus.BORROWED);
    }

    public List<BorrowRecord> getOverdueBooks() {
        Date now = new Date();
        return borrowRecordRepository.findByStatusAndDueDateBefore(BorrowStatus.BORROWED, now);
    }

    public List<BorrowRecord> getBookBorrowRecords(Long bookId) {
        return borrowRecordRepository.findByBookId(bookId);
    }

    public boolean isBookAvailable(Long bookId) {
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new RuntimeException("Book not found with id: " + bookId));
        return book.getAvailable() > 0;
    }

    public int getAvailableCopiesCount(Long bookId) {
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new RuntimeException("Book not found with id: " + bookId));
        return book.getAvailable();
    }

    public long getTotalBorrowedCount(Long userId) {
        return borrowRecordRepository.countByUserIdAndStatus(userId, BorrowStatus.BORROWED);
    }

    @Transactional
    public BorrowRecord renewBook(Long borrowRecordId, int extraDays) {
        BorrowRecord borrowRecord = borrowRecordRepository.findById(borrowRecordId)
            .orElseThrow(() -> new RuntimeException("Borrow record not found with id: " + borrowRecordId));

        if (borrowRecord.getStatus() != BorrowStatus.BORROWED) {
            throw new RuntimeException("Cannot renew - book is not currently borrowed");
        }

        Date currentDueDate = borrowRecord.getDueDate();
        Date newDueDate = new Date(currentDueDate.getTime() + TimeUnit.DAYS.toMillis(extraDays));
        borrowRecord.setDueDate(newDueDate);

        return borrowRecordRepository.save(borrowRecord);
    }

    public BorrowRecord getBorrowRecordById(Long recordId) {
        return borrowRecordRepository.findById(recordId)
            .orElseThrow(() -> new RuntimeException("Borrow record not found with id: " + recordId));
    }

    public List<BorrowRecord> getAllBorrowRecords() {
        return borrowRecordRepository.findAll();
    }

    public double calculateFine(Long borrowRecordId) {
        BorrowRecord borrowRecord = getBorrowRecordById(borrowRecordId);
        
        if (borrowRecord.getStatus() == BorrowStatus.RETURNED) {
            return borrowRecord.getFine();
        }
        
        Date now = new Date();
        if (now.after(borrowRecord.getDueDate())) {
            long diffInMillies = now.getTime() - borrowRecord.getDueDate().getTime();
            long daysOverdue = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            return daysOverdue * 10;
        }
        
        return 0.0;
    }

    @Transactional
    public BorrowRecord payFine(Long borrowRecordId) {
        BorrowRecord borrowRecord = getBorrowRecordById(borrowRecordId);
        borrowRecord.setFine(0.0);
        return borrowRecordRepository.save(borrowRecord);
    }
}