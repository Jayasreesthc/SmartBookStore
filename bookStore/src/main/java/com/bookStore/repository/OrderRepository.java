package com.bookStore.repository;

import com.bookStore.entity.Client;
import com.bookStore.entity.Order;
import com.bookStore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Integer> {
    List<Order> findByClient(Client client);
}
