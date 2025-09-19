package com.bookStore.controller;
import com.bookStore.entity.User;
import com.bookStore.repository.UserRepository;
import com.bookStore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegisterController {

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private UserService userservice;

        // Show registration form
        @GetMapping("/register")
        public String showRegisterForm(Model model) {
            if (!model.containsAttribute("user")) {
                model.addAttribute("user", new User());
            }
            return "Register"; // register.html
        }

        // Handle form submit
        @PostMapping("/register")
        public String registerUser(@RequestParam("fullname") String fullname,
                                   @RequestParam("email") String email,
                                   @RequestParam("username") String username,
                                   @RequestParam("password") String password,
                                   @RequestParam("confirmPassword") String confirmPassword, Model model) {
            // Save user to DB
            User user = new User();
            user.setFullname(fullname);
            user.setEmail(email);
            user.setUsername(username);
            user.setPassword(password); // ⚠️ hashing recommended later

            String message = userservice.registerUser(user, confirmPassword);
            if (message.startsWith("⚠️")) {
                model.addAttribute("message", message);
                model.addAttribute("user", user);
                return "Register";
            }


            model.addAttribute("message", "User registered successfully!");
            return "redirect:/Login_client?success=User registered successfully!"; // redirect to login.html after success
        }


}
