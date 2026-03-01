package com.library.service;

import com.library.model.Book;
import com.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    // Get all books
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Get book by ID
    public Book getBookById(Long id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isPresent()) {
            return optionalBook.get();
        } else {
            throw new RuntimeException("Book not found with id: " + id);
        }
    }

    // Search books by keyword
    public List<Book> searchBooks(String keyword) {
        return bookRepository.searchBooks(keyword);
    }

    // Add new book
    public Book addBook(Book book) {
        // Check if ISBN already exists
        Optional<Book> existingBook = bookRepository.findByIsbn(book.getIsbn());
        if (existingBook.isPresent()) {
            throw new RuntimeException("Book with ISBN " + book.getIsbn() + " already exists!");
        }
        
        // Set available quantity
        if (book.getAvailable() == null) {
            book.setAvailable(book.getQuantity());
        }
        
        return bookRepository.save(book);
    }

    // Update book
    public Book updateBook(Long id, Book bookDetails) {
        Book existingBook = getBookById(id);
        
        existingBook.setTitle(bookDetails.getTitle());
        existingBook.setAuthor(bookDetails.getAuthor());
        existingBook.setPublisher(bookDetails.getPublisher());
        existingBook.setIsbn(bookDetails.getIsbn());
        existingBook.setQuantity(bookDetails.getQuantity());
        existingBook.setAvailable(bookDetails.getAvailable());
        existingBook.setShelfLocation(bookDetails.getShelfLocation());
        existingBook.setDescription(bookDetails.getDescription());
        
        return bookRepository.save(existingBook);
    }

    // Delete book
    public void deleteBook(Long id) {
        Book book = getBookById(id);
        bookRepository.delete(book);
    }

    // Get low stock books
    public List<Book> getLowStockBooks() {
        return bookRepository.findLowStockBooks();
    }

    // Alternative get by ID with exception
    public Book getBookByIdOrThrow(Long id) {
        return bookRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
    }
}