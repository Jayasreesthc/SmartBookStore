package com.bookStore.service;

import com.bookStore.entity.Client;
import com.bookStore.entity.User;
import com.bookStore.repository.ClientRepository;
import com.bookStore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClientRepository crepo;

    public User validateUser(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password)
                .orElse(null);
    }
    public String registerUser(User user, String confirmPassword) {
        // Check if username+email pair already exists
        if (userRepository.existsByUsernameAndEmail(user.getUsername(), user.getEmail())) {
            return "⚠️ This username and email combination already exists.";
        }

        // Check password match
        if (!user.getPassword().equals(confirmPassword)) {
            return "⚠️ Password and Confirm Password do not match.";
        }

        // Save user
        userRepository.save(user);

        // Also save in login table
        boolean clientExists = crepo.findByUsername(user.getUsername()).isPresent();
        if (!clientExists) {
            Client client = new Client();
            client.setUsername(user.getUsername());
            client.setPassword(user.getPassword());
            crepo.save(client);
        }
        return "✅ User registered successfully!";
    }
}
