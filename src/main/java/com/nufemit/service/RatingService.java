package com.nufemit.service;

import com.nufemit.exception.UniqueRatingException;
import com.nufemit.model.Rating;
import com.nufemit.model.User;
import com.nufemit.model.dto.RatingDTO;
import com.nufemit.repository.RatingRepository;
import com.nufemit.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

import static java.lang.Boolean.TRUE;

@Service
@AllArgsConstructor
@Slf4j
public class RatingService {

    private UserRepository userRepository;
    private RatingRepository ratingRepository;

    public Boolean createRating(RatingDTO ratingDTO, User reviewer) {
        log.info("Create rating");
        return userRepository.findById(ratingDTO.getId())
            .map(reviewed -> verifyExistingRating(ratingDTO, reviewer, reviewed))
            .orElseThrow(EntityNotFoundException::new);
    }

    public Boolean deleteRating(Long id, User user) {
        ratingRepository.findByIdAndReviewer(id, user)
            .ifPresent(rating -> ratingRepository.delete(rating));
        log.info("RATING {} deleted", id);
        return TRUE;
    }

    private Boolean verifyExistingRating(RatingDTO ratingDTO, User reviewer, User reviewed) {
        if (ratingRepository.findByReviewerAndReviewed(reviewer, reviewed).isEmpty()) {
            return saveRating(ratingDTO, reviewer, reviewed);
        } else {
            throw new UniqueRatingException();
        }
    }

    private Boolean saveRating(RatingDTO ratingDTO, User reviewer, User reviewed) {
        Integer rating = ratingDTO.getRating();
        if (ratingDTO.getRating() > 5) rating = 5;
        else if (ratingDTO.getRating() < 0) rating = 0;
        ratingRepository.save(Rating.builder()
            .rating(rating)
            .reviewer(reviewer)
            .reviewed(reviewed)
            .comment(ratingDTO.getComment())
            .build());
        return TRUE;
    }
}
