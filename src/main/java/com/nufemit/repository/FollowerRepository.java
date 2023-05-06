package com.nufemit.repository;

import com.nufemit.model.Follower;
import com.nufemit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowerRepository extends JpaRepository<Follower, Long> {
    Optional<Follower> findByFollowerAndFollowed(User follower, User followed);

    Integer countByFollowed(User user);

    Integer countByFollower(User user);

    List<Follower> findByFollowed(User user);

    List<Follower> findByFollower(User user);
}
