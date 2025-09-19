package com.bookStore.controller;

import com.bookStore.entity.*;
import com.bookStore.service.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class bookController {

    @Autowired
    private BookService service;

    @Autowired
    private MyBookListService myBookService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserProfileService upservice;

    @Autowired
    private UserService userservice;


    @GetMapping("/")
      public String home(){
        return "home";
    }

    @GetMapping("/home")
    public String tohome(){
        return "home";
    }

    @GetMapping("/book_register")
    public String bookRegister(){
        return "bookRegister";
    }

    @GetMapping("/Login_admin")
    public String showLoginPage() {
        return "Loginadmin"; // refers to loginadmin.html inside /templates
    }

    @PostMapping("/admin/login")
    public String login(@RequestParam String username,
                      @RequestParam String password,
                       RedirectAttributes redirectAttributes) throws IOException {
        if (adminService.validateAdmin(username, password)) {
            return "redirect:/available_books";
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid username or password");
            return "redirect:/Login_admin";
        }
    }

    @GetMapping("/Login_client")
    public String showCLoginPage() {
        return "Loginclient"; // refers to loginadmin.html inside /templates

    }

    @PostMapping("/client/login")
    public void clientLogin(@RequestParam String username,
                            @RequestParam String password,
                             HttpSession session,
                            Model model,HttpServletResponse response) throws IOException {

        Client user = clientService.validateUser(username, password); // check in DB


        if (user == null) {
            // Not found in Client → check in Register table
            User registeredUser = userservice.validateUser(username, password);
            if (registeredUser != null) {
                //  Create Client object from registered User
                user = new Client();
                user.setUsername(registeredUser.getUsername());
                user.setPassword(registeredUser.getPassword());

            }
        }

        if (user !=null) {
            session.setAttribute("username", user.getUsername());
            session.setAttribute("password", user.getPassword());
            session.setAttribute("loggedInClient", user);
            UserProfile profile = upservice.getByUsername(user.getUsername()).orElse(null);
            if (profile != null) {
                session.setAttribute("userProfile", profile);
            }
            response.sendRedirect("/available_cbooks"); // ✅ success
        } else {
            response.sendRedirect("/register?error=notRegistered"); // ❌ failure
        }
    }


    @GetMapping("/uploadBooks")
    public String uploadBooks(){
        return "uploadBooks";
    }

    @GetMapping("/available_books")
    public ModelAndView getAllBook(){
        List<Book>list=service.getAllBook();
        return new ModelAndView("bookList","book",list);
    }


    @GetMapping("/available_cbooks")
    public ModelAndView getAllCBook(HttpSession session){
        List<Book>list=service.getAllBook();
        Client client = (Client) session.getAttribute("loggedInClient");
        if (client == null) {
            // if session expired → redirect to login
            return new ModelAndView("redirect:/Login_client");
        }
        ModelAndView mv = new ModelAndView("availablecbook");
        mv.addObject("book", list);
        mv.addObject("client", client); // ✅ Add client to the model
        return mv;
    }

    private final String uploadDir = "src/main/resources/static/uploads/";

    @PostMapping("/save")
    public String addBook(
                            @ModelAttribute("book") Book book,
                            @RequestParam("image") MultipartFile imageFile)throws IOException{
        if (!imageFile.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, imageFile.getBytes());

            book.setImagePath("/uploads/" + fileName);
        } else {
            if (book.getId() != 0) {
                Book existing = service.getBookById(book.getId());
                if (existing != null) {
                    book.setImagePath(existing.getImagePath());
                }
            }
        }
            service.save(book);
            return "redirect:/available_books";

    }

    @GetMapping("/book/{id}")
    public String getBookDetails(@PathVariable("id") int id, Model model) {
        Book book = service.getBookById(id);  // service should call repo.findById
        if (book == null) {
            model.addAttribute("errorMessage", "Book not found");
            return "error";
        }
        List<Book> allBooks = service.getAllBook();
        model.addAttribute("book", book);
        model.addAttribute("allbooks", allBooks);
        return "view_details";
    }

    @GetMapping("/mybook/{bookId}")
    public String getmyBookDetails(@PathVariable("bookId") int bookId, Model model) {
        Book book = service.getBookById(bookId);

        List<Book> allBooks = service.getAllBook();
        model.addAttribute("book", book);
        model.addAttribute("allbooks", allBooks);
        return "view_details"; // a new Thymeleaf template
    }

    @PostMapping("/update")
    public String updateBook(@ModelAttribute("book") Book book,
                             @RequestParam(value = "image", required = false) MultipartFile imageFile) {
        service.updateBook(book, imageFile);
        return "redirect:/available_books";
    }


    @GetMapping("/my_books")
    public String getMyBooks(HttpSession session, Model model) {
        Client client = (Client) session.getAttribute("loggedInClient");
        if (client == null) {
            return "redirect:/Login_client";
        }
        List<MyBookList> list = myBookService.getBooksByClient(client.getUsername());
        model.addAttribute("book", list);
        model.addAttribute("client", client);
        return "mybooks";
    }

    @RequestMapping("/myList/{id}")
    public String getMyList(@PathVariable("id") int id,HttpSession session,RedirectAttributes redirectAttributes){
        Client client = (Client) session.getAttribute("loggedInClient"); // store Client object at login
        if (client == null) {
            return "redirect:/Login_client"; // redirect to login if not logged in
        }
        //Book b=service.getBookById(id);
        String result = myBookService.addToMyBooks(id, client.getId());

        redirectAttributes.addFlashAttribute("message", result);

        return "redirect:/my_books";
    }


    @GetMapping("/search")
    public String search(@RequestParam("query") String query, Model model) {
        List<Book> books = service.searchBooks(query);
        model.addAttribute("books", books);
        return "searchResults"; // Thymeleaf template for search results
    }

    //  Autocomplete (returns JSON)
    @ResponseBody
    @GetMapping("/autocomplete")
    public List<Map<String, Object>> autocomplete(@RequestParam("query") String query) {
        return service.autocompleteBooks(query);
    }

    @RequestMapping("/editBook/{id}")
    public String editBook(@PathVariable("id") int id,Model model){
        Book b=service.getBookById(id);
        model.addAttribute("book",b);
        return "bookEdit";
    }


    @RequestMapping("/deleteBook/{id}")
    public String deleteBook(@PathVariable("id")int id)
    {
        myBookService.deleteFromAvailableBooks(id);
        return "redirect:/available_books";
    }



}
