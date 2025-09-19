//package com.bookStore.controller;
//
//import com.bookStore.entity.UserProfile;
//import com.bookStore.service.UserProfileService;
//import jakarta.servlet.http.HttpSession;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//
//@Controller
//@RequestMapping("/profile")
//public class ProfileController {
//
//    @Autowired
//    private UserProfileService upservice;
//
//    private final String uploadDir = "src/main/resources/static/uploads/";
//
//    // Profile page
//    @GetMapping
//    public String profilePage(HttpSession session, Model model) {
//        String username = (String) session.getAttribute("username");
//        String password = (String) session.getAttribute("password");
//        if (username == null) return "redirect:/login";
//
////        UserProfile user = upservice.getByUsername(username).orElse(null);
////        if (user == null) {
////            // fallback if user not found in user_profile
////            user = new UserProfile();
////            user.setUsername(username);
////
////        }
//        UserProfile user = (UserProfile) session.getAttribute("userProfile");
//        if (user == null) {
//            user = upservice.getByUsername(username).orElse(new UserProfile());
//        }
//
//        user.setPassword(password);
//        model.addAttribute("user", user);
//        return "myProfile";
//    }
//
//    // Update profile info
//    @PostMapping("/update")
//    public String updateProfile(@Valid @ModelAttribute("user") UserProfile user,
//                                BindingResult result,
//                                @RequestParam("profilePicFile") MultipartFile file,
//                                HttpSession session,
//                                Model model) throws IOException {
//
//        String username = (String) session.getAttribute("username");
//        String password = (String) session.getAttribute("password");
//        if (username == null) return "redirect:/Login_client";
//
//        if (result.hasErrors()) {
//            model.addAttribute("user", user);
//            return "myProfile";
//        }
//        UserProfile existingUser = upservice.getByUsername(username).orElse(new UserProfile());
//        existingUser.setUsername(username);
//        existingUser.setPassword(password); // keep password updated
//        existingUser.setEmail(user.getEmail());
//        existingUser.setMobile(user.getMobile());
//        existingUser.setBday(user.getBday());
//        //user.setUsername(username);
//
//
//        if (!file.isEmpty()) {
//            Files.createDirectories(Paths.get(uploadDir));
//            String fileName = file.getOriginalFilename();
//            file.transferTo(Paths.get(uploadDir + fileName));
//            existingUser.setProfilePic(fileName);
////            return "/uploads/" + fileName;
//        }
//
//        upservice.updateProfile(existingUser);
//        model.addAttribute("user", existingUser);
//        model.addAttribute("successMsg", "Profile updated successfully!");
//        return "myProfile";
//    }
//
//    // Change password
//    @PostMapping("/changePassword")
//    public String changePassword(@RequestParam("oldPassword") String oldPassword,
//                                 @RequestParam("newPassword") String newPassword,
//                                 @RequestParam("confirmPassword") String confirmPassword,
//                                 HttpSession session,
//                                 Model model) {
//
//        String username = (String) session.getAttribute("username");
//        if (username == null) return "redirect:/Login_client";
//
//        UserProfile user = upservice.getByUsername(username).orElse(null);
//
//        if (user == null) return "redirect:/Login_client";
//        model.addAttribute("user", user);
//
////        if (!upservice.checkOldPassword(username, oldPassword)) {
////            model.addAttribute("errorMsg", "Old password is incorrect!");
////            return "myProfile";
////        }
//
//        if (!newPassword.equals(confirmPassword)) {
//            model.addAttribute("errorMsg", "New password and Confirm password do not match!");
//            return "myProfile";
//        }
//
//        if (upservice.isUsernamePasswordComboExist(username, newPassword)) {
//            model.addAttribute("errorMsg", "This username and password combination already exists, please enter a different password!");
//            return "myProfile";
//        }
//
//        upservice.changePassword(user, newPassword);
//
//        // Auto logout after password change
//        session.invalidate();
//        return "redirect:/Login_client?passwordChanged=true";
//    }
//
//
//}
package com.bookStore.controller;

import com.bookStore.entity.Client;
import com.bookStore.entity.User;
import com.bookStore.entity.UserProfile;
import com.bookStore.repository.ClientRepository;
import com.bookStore.service.UserProfileService;
import com.bookStore.validation.ValidationGroups;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private UserProfileService upservice;

    @Autowired
    private ClientRepository crepo;

    private final String uploadDir = "src/main/resources/static/uploads/";

    // Profile page
    @GetMapping
    public String profilePage(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");

        if (username == null) {
            return "redirect:/Login_client"; // if not logged in, redirect
        }

        UserProfile userProfile = upservice.getByUsername(username)
                .orElseGet(() -> {
                    UserProfile newProfile = new UserProfile();
                    newProfile.setUsername(username);  // ✅ set username
                    return newProfile;
                });
        model.addAttribute("user", userProfile);
        return "myProfile";
    }

    // Update profile info (except password, profilePic)
    @PostMapping("/update")
    public String updateProfile(@Validated(ValidationGroups.OnUpdate.class) @Valid @ModelAttribute("user") UserProfile user,
                                @RequestParam(value = "profilePicFile", required = false) MultipartFile file,
                                BindingResult result,
                                HttpSession session,
                                Model model) throws IOException {

        String username = (String) session.getAttribute("username");
        String password = (String) session.getAttribute("password");
        if (username == null) return "redirect:/Login_client";

        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "myProfile";
        }

        user.setUsername(username);
        user.setPassword(password);
        UserProfile existingUser = upservice.getByUsername(username).orElse(null);
        //if (existingUser == null) return "redirect:/Login_client";

        // update fields
        if (existingUser == null) {
            // 🚀 First time -> create new profile
            existingUser = new UserProfile();
            existingUser.setUsername(username); // username from session
            existingUser.setPassword(password);
        }
        existingUser.setEmail(user.getEmail());
        existingUser.setMobile(user.getMobile());
        existingUser.setBday(user.getBday());


        if (file != null && !file.isEmpty()) {
            Files.createDirectories(Paths.get(uploadDir));
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            file.transferTo(Paths.get(uploadDir, fileName));
            existingUser.setProfilePic(fileName);
        }

        upservice.updateProfile(existingUser);
        model.addAttribute("user", existingUser);
        model.addAttribute("successMsg", "Profile updated successfully!");
        return "myProfile";
    }

    // Profile pic upload (AJAX)
    @PostMapping("/uploadProfilePic")
    @ResponseBody
    public String uploadProfilePic(@RequestParam("profilePicFile") MultipartFile file,
                                   HttpSession session) throws IOException {
        String username = (String) session.getAttribute("username");
        if (username == null) return "Login_client";

        UserProfile user = upservice.getByUsername(username).orElse(null);
        if (user == null) return "Login_client";

        if (!file.isEmpty()) {
            Files.createDirectories(Paths.get(uploadDir));
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            file.transferTo(Paths.get(uploadDir + fileName));

            user.setProfilePic(fileName);
            upservice.updateProfile(user);

            return "/uploads/" + fileName; // return path
        }
        return "NO_FILE";
    }

    // Change password
    @PostMapping("/changePassword")
    public String changePassword(@RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 HttpSession session,
                                 Model model) {

        String username = (String) session.getAttribute("username");
        if (username == null) return "redirect:/Login_client";

        UserProfile user = upservice.getByUsername(username).orElse(null);
        if (user == null) return "redirect:/Login_client";

        model.addAttribute("user", user);

        // check old password
        if (!upservice.checkOldPassword(username, oldPassword)) {
            model.addAttribute("errorMsg", "Old password is incorrect!");
            model.addAttribute("user", user);
            return "myProfile";
        }

        // new and confirm must match
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("errorMsg", "New password and Confirm password do not match!");
            model.addAttribute("user", user);
            return "myProfile";
        }

        // prevent duplicate (same as another user)
        if (upservice.isPasswordUsedByAnotherUser(username, newPassword)) {
            model.addAttribute("errorMsg", "This password is already used by another user, please choose a different one!");
            model.addAttribute("user",user);
            return "myProfile";
        }
        model.addAttribute("successMsg", "New Password updated successfully!");
        upservice.changePassword(user, newPassword);


        Optional<Client> clientOpt = crepo.findByUsername(username);
        if (clientOpt.isPresent()) {
            Client client = clientOpt.get();
            client.setPassword(newPassword);
            crepo.save(client);
        }


        // Auto logout after password change
        session.invalidate();
        return "redirect:/Login_client?passwordChanged=true";
    }
}
