package com.nufemit.service;

import com.nufemit.model.Activity;
import com.nufemit.model.User;
import com.nufemit.repository.ActivityRepository;
import com.nufemit.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@Slf4j
public class RegistrationService {

    private ActivityRepository activityRepository;
    private UserRepository userRepository;

    public Boolean registerUser(Long activityId, Long userId) {
        return activityRepository.findByIdAndDateTimeGreaterThanEqual(activityId, LocalDateTime.now())
            .map(activity -> fetchUserAndRegister(activity, userId))
            .orElseThrow(EntityNotFoundException::new);
    }

    private Boolean fetchUserAndRegister(Activity activity, Long userId) {
        return userRepository.findById(userId)
            .map(user -> addUserToActivity(activity, user))
            .orElseThrow(EntityNotFoundException::new);
    }

    private Boolean addUserToActivity(Activity activity, User user) {
        if (activity.getUsers().contains(user)) {
            log.warn("User {} already registered for the activity {}", user.getId(), activity.getId());
            return Boolean.FALSE;
        }
        activity.addUser(user);
        activityRepository.save(activity);
        return Boolean.TRUE;
    }
}
