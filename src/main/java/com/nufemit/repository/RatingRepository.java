package com.nufemit.repository;

import com.nufemit.model.Rating;
import com.nufemit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    List<Rating> findByReviewerAndReviewed(User user, User reviewed);

    Optional<Rating> findByIdAndReviewer(Long id, User user);

    List<Rating> findByReviewed(Long id);
}
