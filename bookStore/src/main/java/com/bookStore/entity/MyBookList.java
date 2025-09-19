package com.bookStore.entity;

import jakarta.persistence.*;

@Entity
@Table(name="MyBooks",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"client_id", "book_id"})
        })
public class MyBookList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String author;
    private double price;

    private String imagePath;

    @ManyToOne
    @JoinColumn(name = "client_id") // FK column
    private Client client;

    @ManyToOne
    @JoinColumn(name="book_id")
    private Book book;


    public MyBookList(Book book, Client client) {
        super();
        //this.id = book.getId();
        this.name = book.getName();
        this.author = book.getAuthor();
        this.price = book.getPrice();
        this.imagePath = book.getImagePath();
        this.book = book;
        this.client = client;
    }
    public MyBookList(){
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }


    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

}
