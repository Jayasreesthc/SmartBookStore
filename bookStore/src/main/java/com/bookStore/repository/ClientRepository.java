package com.bookStore.repository;

import com.bookStore.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client,Long> {
    Client findByUsernameAndPassword(String username, String password);
    Optional<Client> findByUsername(String username);
}


