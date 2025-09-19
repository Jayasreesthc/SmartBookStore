package com.bookStore.repository;

import com.bookStore.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface bookRepository extends JpaRepository<Book,Integer> {

    List<Book> findByNameContainingIgnoreCaseOrAuthorContainingIgnoreCase(String name, String author);

    List<Book> findTop5ByNameContainingIgnoreCaseOrAuthorContainingIgnoreCase(String name, String author);

}
