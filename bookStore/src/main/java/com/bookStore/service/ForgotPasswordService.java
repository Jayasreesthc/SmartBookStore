package com.bookStore.service;

import com.bookStore.entity.User;
import com.bookStore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;



import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
public class ForgotPasswordService {

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private JavaMailSender mailSender;

        private Map<String, String> otpStorage = new HashMap<>(); // store OTP temporarily (username → otp)

        public String sendOtp(String username, String email) {
            User user = userRepository.findByUsernameAndEmail(username, email).orElse(null);

            if (user == null) {
                return "⚠️ User not found";
            }

            // generate 6-digit OTP
            String otp = String.format("%06d", new Random().nextInt(999999));

            // store OTP temporarily
            otpStorage.put(username, otp);

            // send OTP email
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Password Reset OTP");
            message.setText("Hello " + username + ",\n\nYour OTP is: " + otp + "\n\nUse this to reset your password.");
            mailSender.send(message);

            return "✅ OTP sent successfully to " + email;
        }

        public boolean validateOtp(String username, String otp) {
            String savedOtp = otpStorage.get(username);
            if (savedOtp != null && savedOtp.equals(otp)) {
                otpStorage.remove(username); // remove after use
                return true;
            }
            return false;
        }



    public boolean updatePassword(String username, String newPassword) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setPassword(newPassword); //  For production: encrypt with BCrypt
            userRepository.save(user);
            return true;
        }
        return false;
    }

}
