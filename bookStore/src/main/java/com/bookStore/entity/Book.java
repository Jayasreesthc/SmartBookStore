package com.bookStore.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Data
public class Book {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String author;
    private String isbn;
    private double price;

    private String imagePath;

    private int quantity;

    private String publisher;

    private String category;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseddate;

    private String language;

    private String noofpages;

    @Column(columnDefinition = "TEXT")
    private String description;

}
