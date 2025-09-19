package com.bookStore.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name="UserOrders")
public class Userorder {

        @Id
        @GeneratedValue
        private Long id;
        private Date date;
        private int itemCount;
        private double totalAmount;

        @ManyToOne
        @JoinColumn(name = "user_id")   // foreign key column in UserOrders table
        private Client user;


}
