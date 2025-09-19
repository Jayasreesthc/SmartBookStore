package com.bookStore.repository;

import com.bookStore.entity.Order;
import com.bookStore.entity.Userorder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserOrderRepository extends JpaRepository<Userorder,Long> {
    List<Userorder> findByUserUsername(String username);
}
