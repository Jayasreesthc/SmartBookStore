package com.bookStore.entity;
import jakarta.persistence.*;
import jdk.jfr.DataAmount;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User {
//entity of user list
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        private String fullname;
        private String email;
        private String username;
        private String password;

        @Transient
        private String confirmPassword;

}
