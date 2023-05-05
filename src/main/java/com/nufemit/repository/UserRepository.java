package com.nufemit.repository;

import com.nufemit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndPassword(String email, String encryptedPassword);

    @Query("SELECT u FROM User u WHERE u.name like CONCAT('%', ?1, '%') or" +
            " u.lastname like CONCAT('%', ?2, '%') or" +
            " u.secondLastname like CONCAT('%', ?3, '%') or" +
            " u.email like CONCAT('%', ?4, '%')")
    List<User> findBySearchBox(String name, String lastname, String secondLastname, String email);

    List<User> findTop25By();
}
