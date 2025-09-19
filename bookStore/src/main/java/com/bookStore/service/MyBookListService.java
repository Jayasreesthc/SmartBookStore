package com.bookStore.service;

import com.bookStore.entity.Book;
import com.bookStore.entity.Client;
import com.bookStore.entity.MyBookList;
import com.bookStore.entity.Order;
import com.bookStore.repository.ClientRepository;
import com.bookStore.repository.MyBookRepository;
import com.bookStore.repository.bookRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MyBookListService {

      @Autowired
      private MyBookRepository mybook;

      @Autowired
      private bookRepository bRepo;

    @Autowired
    private ClientRepository crepo;
      public void saveMyBooks(MyBookList book){
          mybook.save(book);
      }


    public List<MyBookList> getBooksByClient(String username) {

        Client client = crepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Client not found"));
          return mybook.findByClient(client);
    }

    @Transactional
    public void deleteFromAvailableBooks(int id) {
        // Delete from client available books
        mybook.deleteByBookId(id);

        // Delete from available books
        bRepo.deleteById(id);
    }

    public String addToMyBooks(int bookId, Long clientId) {
        Book book = bRepo.findById(bookId).orElseThrow();
        Client client = crepo.findById(clientId).orElseThrow();

        boolean exists = mybook.existsByBookIdAndClientId(bookId, clientId);
        if (exists) {
            return "Book already in MyBooks!";
        }

        if (book.getName() == null || book.getName().trim().isEmpty() ||
                book.getAuthor() == null || book.getAuthor().trim().isEmpty() ||
                book.getPrice() <=0 || book.getPrice() <= 0) {
            return "Invalid book details. Cannot add to MyBooks.";
        }

        MyBookList myBook = new MyBookList(book,client);
        myBook.setName(book.getName());
        myBook.setAuthor(book.getAuthor());
        myBook.setPrice(book.getPrice());
        myBook.setImagePath(book.getImagePath());
        myBook.setBook(book);
        myBook.setClient(client);


        mybook.save(myBook);
        return "Book added successfully!";
    }

    /**
     * Buy a book (no quantity check for now)
     */
    public String buyBook(int bookId, Long userId) {
        Book book = bRepo.findById(bookId).orElseThrow();
        Client client = crepo.findById(userId).orElseThrow();

        // Example: Save order record (optional, depends on your design)
        MyBookList mb = new MyBookList(book,client);
        mybook.save(mb);
        // orderRepository.save(order);  // if you have an Order repository

        return "Book purchased successfully!";
    }

    public MyBookList getMyBookById(int id){
        return mybook.findById(id).get();
    }

      public void deleteFromMyBooks(int myBookListid, Long clientId)
      {
          mybook.deleteByIdAndClientId(myBookListid, clientId);
      }
}



