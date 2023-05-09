package com.nufemit.service;

import com.nufemit.exception.AuthenticationException;
import com.nufemit.exception.DuplicateInformationException;
import com.nufemit.model.Follower;
import com.nufemit.model.Rating;
import com.nufemit.model.User;
import com.nufemit.model.dto.LoginDTO;
import com.nufemit.model.dto.ProfileDTO;
import com.nufemit.model.dto.ResponseDTO;
import com.nufemit.repository.FollowerRepository;
import com.nufemit.repository.RatingRepository;
import com.nufemit.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

import static com.nufemit.utils.CredentialsUtils.createToken;
import static com.nufemit.utils.CredentialsUtils.encrypt;
import static java.lang.Boolean.TRUE;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private UserRepository userRepository;
    private RatingRepository ratingRepository;
    private FollowerRepository followerRepository;

    public List<User> getUsers(String searchBox) {
        if (searchBox == null || searchBox.isBlank()) {
            return userRepository.findTop25By();
        }
        return userRepository.findTop25BySearchBox(searchBox, searchBox, searchBox, searchBox, PageRequest.of(0, 25));
    }

    public ProfileDTO getUsersById(Long id, User user) {
        return userRepository.findById(id)
            .map(userFetched -> mapToProfile(userFetched, user))
            .orElseThrow(EntityNotFoundException::new);
    }

    public Boolean createUser(User user) {
        user.setPassword(encrypt(user.getPassword()));
        try {
            userRepository.save(user);
            log.info("New USER created: {}", user.getId());
        } catch (Exception e) {
            throw new DuplicateInformationException();
        }
        return TRUE;
    }

    public ResponseDTO loginUser(LoginDTO loginDTO) {
        return userRepository.findByEmailAndPassword(loginDTO.getEmail(), encrypt(loginDTO.getPassword()))
            .map(user -> createToken(user.getId(), user.getEmail(), user.getPassword()))
            .map(token -> ResponseDTO.builder().token(token).build())
            .orElseThrow(AuthenticationException::new);
    }

    public Boolean deleteUser(User user) {
        userRepository.deleteById(user.getId());
        log.info("USER {} deleted", user.getId());
        return TRUE;
    }

    public Boolean followUser(Long id, User follower) {
        userRepository.findById(id)
            .ifPresent(followed -> followerRepository.findByFollowerAndFollowed(follower, followed)
                .ifPresentOrElse(followObj -> log.info("User {} is already following User {}", followObj.getFollower().getId(), followObj.getFollowed().getId()),
                    () -> followerRepository.save(Follower.builder().follower(follower).followed(followed).build())));
        return TRUE;
    }

    public Boolean unfollowUser(Long id, User follower) {
        userRepository.findById(id)
            .flatMap(followed -> followerRepository.findByFollowerAndFollowed(follower, followed))
            .ifPresent(followObj -> followerRepository.delete(followObj));
        return TRUE;
    }

    public List<User> getFollowers(User user) {
        return followerRepository.findByFollowed(user).stream()
            .map(Follower::getFollower)
            .collect(Collectors.toList());
    }

    public List<User> getFollowersForUser(Long id) {
        return userRepository.findById(id)
            .map(user -> followerRepository.findByFollowed(user).stream()
                .map(Follower::getFollower)
                .collect(Collectors.toList()))
            .orElseThrow(EntityNotFoundException::new);
    }

    public List<User> getFollowing(User user) {
        return followerRepository.findByFollower(user).stream()
            .map(Follower::getFollowed)
            .collect(Collectors.toList());
    }

    public List<User> getFollowingForUser(Long id) {
        return userRepository.findById(id)
            .map(user -> followerRepository.findByFollower(user).stream()
                .map(Follower::getFollowed)
                .collect(Collectors.toList()))
            .orElseThrow(EntityNotFoundException::new);
    }

    private ProfileDTO mapToProfile(User userFetched, User user) {
        return ProfileDTO.builder()
            .user(userFetched)
            .rating(calculateRating(userFetched))
            .followers(followerRepository.countByFollowed(userFetched))
            .following(followerRepository.countByFollower(userFetched))
            .followedByUser(followerRepository.findByFollowed(userFetched).stream()
                .anyMatch(follower -> follower.getFollower().equals(user)))
            .activities(userFetched.getActivitiesJoined().size())
            .build();
    }

    private Double calculateRating(User user) {
        return ratingRepository.findByReviewed(user).stream()
            .mapToDouble(Rating::getRating)
            .average()
            .orElse(-1);
    }
}
