package com.library.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "borrow_records")
public class BorrowRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;  // This maps to Book
    
    @Temporal(TemporalType.DATE)
    private Date borrowDate;
    
    @Temporal(TemporalType.DATE)
    private Date dueDate;
    
    @Temporal(TemporalType.DATE)
    private Date returnDate;
    
    @Enumerated(EnumType.STRING)
    private BorrowStatus status;
    
    private Double fine;
    
    // Constructors
    public BorrowRecord() {}
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }
    
    public Date getBorrowDate() { return borrowDate; }
    public void setBorrowDate(Date borrowDate) { this.borrowDate = borrowDate; }
    
    public Date getDueDate() { return dueDate; }
    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }
    
    public Date getReturnDate() { return returnDate; }
    public void setReturnDate(Date returnDate) { this.returnDate = returnDate; }
    
    public BorrowStatus getStatus() { return status; }
    public void setStatus(BorrowStatus status) { this.status = status; }
    
    public Double getFine() { return fine; }
    public void setFine(Double fine) { this.fine = fine; }
}