package com.bookStore.entity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

       // who buys
    private String bookName;
    private String author;
    private double price;
    private String isbn;
    private int quantity;
    private String status;
    //private String currentStatus = "PLACED";
    private Integer deliveryDays = 3;
    @Column(nullable = false)
    private LocalDate orderDate = LocalDate.now();
    private String address;
    private String phoneNumber;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime lastUpdated;

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        lastUpdated =LocalDateTime.now();;
    }


    @ManyToOne
    @JoinColumn(name="book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "client_id") // FK column
    private Client client;


}
