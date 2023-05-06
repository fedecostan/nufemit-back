package com.nufemit.repository;

import com.nufemit.model.Rating;
import com.nufemit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    List<Rating> findByReviewerAndReviewed(User user, User reviewed);

    Optional<Rating> findByIdAndReviewer(Long id, User user);

    List<Rating> findByReviewed(User user);
}
