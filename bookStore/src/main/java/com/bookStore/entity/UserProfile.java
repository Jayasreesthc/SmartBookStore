package com.bookStore.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "user_profile")
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id") // foreign key
    private Client client;

    @NotBlank(message = "Username is required")
    @Column(unique = true)
    private String username;

    //@NotBlank(message = "Password is required")
    private String password;

    //@NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(unique = true)
    private String email;

    //@NotBlank(message = "Mobile is required")
    @Pattern(regexp = "\\d{10}", message = "Mobile must be 10 digits")
    private String mobile;

    private LocalDate bday;

    private String profilePic; // store file name


}


