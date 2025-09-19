package com.bookStore.controller;

import com.bookStore.entity.User;
import com.bookStore.repository.UserRepository;
import com.bookStore.service.ForgotPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ForgotPasswordController {

        @Autowired
        private ForgotPasswordService forgotPasswordService;

        @Autowired
        private UserRepository userRepository;

        @GetMapping("/forgot-password")
        public String showForgotPasswordPage() {
            return "Forgot_password"; // Thymeleaf page
        }

        @PostMapping("/forgot-password")
        public String processForgotPassword(@RequestParam String username,
                                            @RequestParam String email,
                                            RedirectAttributes redirectAttributes) {
            String result = forgotPasswordService.sendOtp(username, email);

            if (result.startsWith("⚠️")) {
                redirectAttributes.addFlashAttribute("error", result);
                return "redirect:/Forgot_password";
            } else {
                redirectAttributes.addFlashAttribute("success", result);
                redirectAttributes.addFlashAttribute("username", username);
                return "redirect:/verify-otp";
            }
        }

        @GetMapping("/verify-otp")
        public String showVerifyOtpPage() {
            return "verify_otp"; // Thymeleaf page
        }

        @PostMapping("/verify-otp")
        public String verifyOtp(@RequestParam String username,
                                @RequestParam String otp,
                                RedirectAttributes redirectAttributes) {
            boolean valid = forgotPasswordService.validateOtp(username, otp);

            if (valid) {
                return "redirect:/reset-password?username=" + username; // ✅ success
            } else {
                redirectAttributes.addFlashAttribute("error", "⚠️ Invalid OTP");
                redirectAttributes.addFlashAttribute("username", username);
                return "redirect:/verify-otp";
            }
        }

    @GetMapping("/reset-password")
    public String showResetPasswordPage(@RequestParam String username, Model model) {
        model.addAttribute("username", username);
        return "reset_password";
    }


    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String username,
                                @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                RedirectAttributes redirectAttributes) {
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "⚠️ Passwords do not match");
            redirectAttributes.addFlashAttribute("username", username);
            return "reset_password";
        }

        //  Call service to update password
        boolean updated = forgotPasswordService.updatePassword(username, newPassword);

        if (updated) {
            redirectAttributes.addFlashAttribute("success", "✅ Password reset successful! Please login.");
            return "Loginclient"; // after reset, go to login page
        } else {
            redirectAttributes.addFlashAttribute("error", "⚠️ Failed to reset password. Try again.");
            redirectAttributes.addFlashAttribute("username", username);
            return "reset_password";
        }
    }

}
