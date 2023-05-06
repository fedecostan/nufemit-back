package com.nufemit.service;

import com.nufemit.exception.UniqueRatingException;
import com.nufemit.model.Rating;
import com.nufemit.model.User;
import com.nufemit.repository.RatingRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static java.lang.Boolean.TRUE;

@Service
@AllArgsConstructor
@Slf4j
public class RatingService {

    private RatingRepository ratingRepository;

    public Boolean createRating(Rating rating, User user) {
        if (!ratingRepository.findByReviewerAndReviewed(user, rating.getReviewed()).isEmpty()) {
            throw new UniqueRatingException();
        }
        rating.setReviewer(user);
        if (rating.getRating() > 5) rating.setRating(5);
        else if (rating.getRating() < 0) rating.setRating(0);
        ratingRepository.save(rating);
        return TRUE;
    }

    public Boolean deleteRating(Long id, User user) {
        ratingRepository.findByIdAndReviewer(id, user)
            .ifPresent(rating -> ratingRepository.delete(rating));
        log.info("RATING {} deleted", id);
        return TRUE;
    }
}
