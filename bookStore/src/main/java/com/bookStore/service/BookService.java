package com.bookStore.service;

import com.bookStore.entity.Book;
import com.bookStore.entity.MyBookList;
import com.bookStore.repository.MyBookRepository;
import com.bookStore.repository.bookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private bookRepository bRepo;

    @Autowired
    private MyBookRepository mybook;


    private final String uploadDir = "src/main/resources/static/uploads/";
    public void save(Book book)throws IOException {

        bRepo.save(book);

    }

    public void updateBook(Book updatedBook, MultipartFile imageFile) {
        // Fetch the existing book from DB
        Book existingBook = bRepo.findById(updatedBook.getId())
                .orElseThrow(() -> new RuntimeException("Book not found with ID: " + updatedBook.getId()));

        // Update fields
        existingBook.setName(updatedBook.getName());
        existingBook.setAuthor(updatedBook.getAuthor());
        existingBook.setPrice(updatedBook.getPrice());
        existingBook.setIsbn(updatedBook.getIsbn());
        existingBook.setQuantity(updatedBook.getQuantity());
        existingBook.setPublisher(updatedBook.getPublisher());
        existingBook.setCategory(updatedBook.getCategory());
        existingBook.setReleaseddate(updatedBook.getReleaseddate());
        existingBook.setLanguage(updatedBook.getLanguage());
        existingBook.setNoofpages(updatedBook.getNoofpages());
        existingBook.setDescription(updatedBook.getDescription());

        // Handle image upload if provided
        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                Path filePath = Paths.get(uploadDir, fileName);
                Files.createDirectories(filePath.getParent());
                Files.write(filePath, imageFile.getBytes());

                existingBook.setImagePath("/uploads/" + fileName); // update image path
            }
        } catch (IOException e) {
            throw new RuntimeException("Error while uploading image", e);
        }

        // Save back to DB (this will perform update)
        bRepo.save(existingBook);
    }


    public List<Book> getAllBook(){
        return  bRepo.findAll();
    }

    public Book getBookById(int id){
        return bRepo.findById(id).get();
    }

    public List<Book> searchBooks(String query) {
        return bRepo.findByNameContainingIgnoreCaseOrAuthorContainingIgnoreCase(query, query);
    }

    // Autocomplete (limited suggestions)
    public List<Map<String, Object>> autocompleteBooks(String query) {
        List<Book> books = bRepo.findTop5ByNameContainingIgnoreCaseOrAuthorContainingIgnoreCase(query, query);

        return books.stream().map(book -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", book.getId());
            map.put("name", book.getName());
            map.put("author", book.getAuthor());
            return map;
        }).toList();
    }




    public Optional<Book> getBookOptionalById(int id) {
        return bRepo.findById(id);
    }

    public void deleteById(int id){
        bRepo.deleteById(id);
    }
}
