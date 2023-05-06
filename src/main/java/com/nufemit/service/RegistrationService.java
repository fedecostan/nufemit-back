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

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

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

    public Boolean unregisterUser(Long activityId, Long userId) {
        return activityRepository.findByIdAndDateTimeGreaterThanEqual(activityId, LocalDateTime.now())
            .map(activity -> fetchUserAndUnregister(activity, userId))
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
            return FALSE;
        }
        activity.addUser(user);
        activityRepository.save(activity);
        return TRUE;
    }

    private Boolean fetchUserAndUnregister(Activity activity, Long userId) {
        return userRepository.findById(userId)
            .map(user -> removeUserFromActivity(activity, user))
            .orElseThrow(EntityNotFoundException::new);
    }

    private Boolean removeUserFromActivity(Activity activity, User user) {
        if (activity.getUsers().contains(user)) {
            activity.removeUser(user);
            activityRepository.save(activity);
            return TRUE;
        }
        log.warn("User {} was not registered in the activity {}", user.getId(), activity.getId());
        return FALSE;
    }
}
