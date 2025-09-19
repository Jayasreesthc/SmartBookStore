package com.bookStore.repository;

import com.bookStore.entity.Client;
import com.bookStore.entity.MyBookList;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MyBookRepository extends JpaRepository<MyBookList,Integer> {
    List<MyBookList> findByClient(Client client);

    boolean existsByBookIdAndClientId(int bookId, Long clientId);

    @Modifying
    @Transactional
    @Query("delete from MyBookList m where m.book.id = :bookId")
    void deleteByBookId(@Param("bookId") int bookId);

    @Modifying
    @Transactional
    @Query("delete from MyBookList m where m.id = :id and m.client.id = :clientId")
    void deleteByIdAndClientId(@Param("id") int id, @Param("clientId") Long clientId);

}



