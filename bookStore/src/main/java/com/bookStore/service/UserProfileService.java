//package com.bookStore.service;
//
//import com.bookStore.entity.UserProfile;
//import com.bookStore.repository.UserProfileRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@Service
//public class UserProfileService {
//    @Autowired
//    private UserProfileRepository uprepo;
//
//    public Optional<UserProfile> getByUsername(String username) {
//        return uprepo.findByUsername(username);
//    }
//
//    public UserProfile updateProfile(UserProfile user) {
//        return uprepo.save(user);
//    }
//
//    public boolean isEmailExist(String email) {
//        return uprepo.findByEmail(email).isPresent();
//    }
//
//    public boolean checkOldPassword(String username, String oldPassword) {
//        Optional<UserProfile> userOpt = uprepo.findByUsername(username);
//        return userOpt.map(u -> u.getPassword().equals(oldPassword)).orElse(false);
//    }
//
//    public boolean isUsernamePasswordComboExist(String username, String password) {
//        Optional<UserProfile> userOpt = uprepo.findByUsernameAndPassword(username, password);
//        return userOpt.isPresent();
//    }
//
//    public void changePassword(UserProfile user, String newPassword) {
//        user.setPassword(newPassword);
//        uprepo.save(user);
//    }
//}
//

package com.bookStore.service;

import com.bookStore.entity.UserProfile;
import com.bookStore.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserProfileService {
    @Autowired
    private UserProfileRepository uprepo;

    public Optional<UserProfile> getByUsername(String username) {
        return uprepo.findByUsername(username);
    }

    public UserProfile updateProfile(UserProfile user) {
        return uprepo.save(user);
    }

    public boolean isEmailExist(String email) {
        return uprepo.findByEmail(email).isPresent();
    }

    public boolean checkOldPassword(String username, String oldPassword) {
        Optional<UserProfile> userOpt = uprepo.findByUsername(username);
        return userOpt.map(u -> u.getPassword().equals(oldPassword)).orElse(false);
    }

    public void changePassword(UserProfile user, String newPassword) {
        user.setPassword(newPassword);
        uprepo.saveAndFlush(user);
        uprepo.save(user);
    }

    // Prevent duplicate password across different users
    public boolean isPasswordUsedByAnotherUser(String username, String password) {
        return uprepo.findByPassword(password)
                .filter(u -> !u.getUsername().equals(username))
                .isPresent();
    }
}

