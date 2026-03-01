package com.library.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "books")
public class Book {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true)
    private String isbn;
    
    private String title;
    private String author;
    private String publisher;
    private Integer quantity;
    private Integer available;
    private String shelfLocation;
    private String description;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date addedDate;
    
    // This field must exist for the mappedBy in Category.java
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;  // This is what 'mappedBy="category"' refers to
    
    @PrePersist
    protected void onCreate() {
        addedDate = new Date();
    }
    
    // Constructors
    public Book() {}
    
    public Book(String isbn, String title, String author, String publisher, Integer quantity) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.quantity = quantity;
        this.available = quantity;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    
    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }
    
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    
    public Integer getAvailable() { return available; }
    public void setAvailable(Integer available) { this.available = available; }
    
    public String getShelfLocation() { return shelfLocation; }
    public void setShelfLocation(String shelfLocation) { this.shelfLocation = shelfLocation; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Date getAddedDate() { return addedDate; }
    public void setAddedDate(Date addedDate) { this.addedDate = addedDate; }
    
    // Add this getter and setter for category
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
}