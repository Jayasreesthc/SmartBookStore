package com.bookStore.controller;

import com.bookStore.entity.*;
import com.bookStore.repository.OrderRepository;
import com.bookStore.repository.UserOrderRepository;
import com.bookStore.service.BookService;
import com.bookStore.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class OrderController {

        @Autowired
        private BookService bookService;

        @Autowired
        private OrderService orderService;

        @Autowired
        private OrderRepository orderRepo;

        @GetMapping("/buy/{id}")
        public String buyBook(@PathVariable("id") int id, Model model) {
            Optional<Book> optionalBook  = bookService.getBookOptionalById(id);
            if(optionalBook.isPresent()) {
                Book book = optionalBook.get();
                Order order = new Order();
                order.setBookName(book.getName());
                order.setAuthor(book.getAuthor());
                order.setPrice(book.getPrice());
                order.setIsbn(book.getIsbn());

                model.addAttribute("order", order);
                return "buy_form";
            }
            else {
                model.addAttribute("Message", "Book not found for ID: " + id);
                return "outofstock";  // create an outofstock.html page in templates
            }
        }

        @PostMapping("/saveOrder")
        public String saveOrder(@ModelAttribute ("order") Order order, HttpSession session, Model model) {
            // Get logged-in user from session
            Client client = (Client) session.getAttribute("loggedInUser");

            // Link order with user
            order.setClient(client);

            order.setStatus("PENDING");            // default status
            order.setOrderDate(LocalDate.now());
            Order savedOrder = orderService.saveOrder(order); // save service

            model.addAttribute("orderId", savedOrder.getId());
            model.addAttribute("orderDate", savedOrder.getOrderDate());
            model.addAttribute("itemCount", savedOrder.getQuantity());
            model.addAttribute("totalAmount", savedOrder.getPrice() * savedOrder.getQuantity());

            model.addAttribute("message", "Order placed successfully!");
            return "order_success";
        }


    // Show all orders for Admin
    @GetMapping("/orders")
    public String viewAllOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "Ordered_information";
    }


    @PostMapping("/orders/updateStatusAjax/{id}")
    @ResponseBody
    public ResponseEntity<?> updateOrderStatusAjax(
            @PathVariable Integer id,
            @RequestParam String status) {

        try {
            orderService.updateStatus(id, status); // your service updates DB
            Map<String,Object> resp = new HashMap<>();
            resp.put("success", true);
            resp.put("status", status);
            resp.put("message", "Order #" + id + " marked as " + status);
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            Map<String,Object> resp = new HashMap<>();
            resp.put("success", false);
            resp.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp);
        }
    }

    @GetMapping("/my_orders")
    public String viewOrders(HttpSession session, Model model) {
        Client loggedInUser = (Client) session.getAttribute("loggedInUser");
        List<Order> myOrders = orderService.getOrdersByClient(loggedInUser);
        model.addAttribute("orders", myOrders);
        return "Order_list"; // Order_list.html (Thymeleaf) or Order_list.jsp
    }

    @GetMapping("/track-order/{id}")
    public String trackOrder(@PathVariable int id, Model model) {
        Order order = orderRepo.findById(id).orElseThrow();
        model.addAttribute("order", order);
        return "track_order";
    }


}
