package com.nufemit.service;

import com.nufemit.model.Activity;
import com.nufemit.model.User;
import com.nufemit.repository.ActivityRepository;
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

    public Boolean registerUser(Long activityId, User user) {
        return activityRepository.findByIdAndDateTimeGreaterThanEqual(activityId, LocalDateTime.now())
            .map(activity -> addUserToActivity(activity, user))
            .orElseThrow(EntityNotFoundException::new);
    }

    public Boolean unregisterUser(Long activityId, User user) {
        return activityRepository.findByIdAndDateTimeGreaterThanEqual(activityId, LocalDateTime.now())
            .map(activity -> removeUserFromActivity(activity, user))
            .orElseThrow(EntityNotFoundException::new);
    }

    public void unregisterUserFromAllActivities(User user) {
        activityRepository.findByParticipantsContains(user)
            .forEach(activity -> removeUserFromActivity(activity, user));
    }

    private Boolean addUserToActivity(Activity activity, User user) {
        if (activity.getParticipants().contains(user)) {
            log.warn("User {} already registered for the activity {}", user.getId(), activity.getId());
            return FALSE;
        }
        activity.addUser(user);
        activityRepository.save(activity);
        return TRUE;
    }

    private Boolean removeUserFromActivity(Activity activity, User user) {
        if (activity.getParticipants().contains(user)) {
            activity.removeUser(user);
            activityRepository.save(activity);
            return TRUE;
        }
        log.warn("User {} was not registered in the activity {}", user.getId(), activity.getId());
        return FALSE;
    }
}
