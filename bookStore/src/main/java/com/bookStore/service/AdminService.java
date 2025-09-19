package com.bookStore.service;

import org.springframework.stereotype.Service;

@Service
public class AdminService {
    private static final String FIXED_USERNAME = "admin";
    private static final String FIXED_PASSWORD = "admin123";

    public boolean validateAdmin(String username, String password) {
        return FIXED_USERNAME.equals(username) && FIXED_PASSWORD.equals(password);
    }
}
