package com.nufemit.repository;

import com.nufemit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndPassword(String email, String encryptedPassword);

    List<User> findByNameContainsOrLastnameContainsOrSecondLastNameContainsOrEmailContains(String searchBox, String searchBox1, String searchBox2, String searchBox3);
}
