package com.nufemit.repository;

import com.nufemit.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndPassword(String email, String encryptedPassword);

    @Query("SELECT u FROM User u WHERE u.name like CONCAT('%', ?1, '%') or" +
        " u.lastname like CONCAT('%', ?2, '%') or" +
        " u.secondLastname like CONCAT('%', ?3, '%') or" +
        " u.email like CONCAT('%', ?4, '%')")
    List<User> findTop25BySearchBox(String name, String lastname, String secondLastname, String email, Pageable pageable);

    List<User> findTop25By();

    Optional<User> findByIdAndEmailAndPassword(Long id, String email, String password);
}
