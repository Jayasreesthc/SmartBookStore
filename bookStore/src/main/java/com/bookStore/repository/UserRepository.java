package com.bookStore.repository;

import com.bookStore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    User findByEmail(String email);

    Optional<User> findByUsernameAndPassword(String username, String password);
    boolean existsByUsernameAndEmail(String username, String email);


    Optional<User> findByUsernameAndEmail(String username, String email);


}
