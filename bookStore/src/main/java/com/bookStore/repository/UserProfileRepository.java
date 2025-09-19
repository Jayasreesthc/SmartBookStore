//package com.bookStore.repository;
//
//import com.bookStore.entity.UserProfile;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.Optional;
//
//public interface UserProfileRepository extends JpaRepository<UserProfile,Integer> {
//    Optional<UserProfile> findByUsername(String username);
//
//    Optional<UserProfile> findByEmail(String email);
//    Optional<UserProfile> findByUsernameAndPassword(String username, String password);
//
//}
//

package com.bookStore.repository;

import com.bookStore.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile,Integer> {
    Optional<UserProfile> findByUsername(String username);
    Optional<UserProfile> findByEmail(String email);

    // for checking duplicate password
    Optional<UserProfile> findByPassword(String password);
}
