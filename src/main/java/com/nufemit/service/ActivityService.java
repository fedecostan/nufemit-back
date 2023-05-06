package com.nufemit.service;

import com.nufemit.model.Activity;
import com.nufemit.model.User;
import com.nufemit.repository.ActivityRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

import static java.lang.Boolean.TRUE;

@Service
@AllArgsConstructor
@Slf4j
public class ActivityService {

    private ActivityRepository activityRepository;

    public Boolean createActivity(Activity activity, User user) {
        activity.setCreator(user);
        Activity newActivity = activityRepository.save(activity);
        log.info("New ACTIVITY created: {}", newActivity.getId());
        return TRUE;
    }

    public List<Activity> getActivities(String searchBox) {
        if (searchBox == null || searchBox.isBlank()) {
            return activityRepository.findTop25ByDateTimeGreaterThanEqualOrderByDateTimeDesc(LocalDateTime.now());
        }
        return activityRepository.findBySearchBox(LocalDateTime.now(), searchBox, searchBox, searchBox);
    }

    public Activity getActivitiesById(Long id) {
        return activityRepository.findById(id)
            .orElseThrow(EntityNotFoundException::new);
    }

    public Boolean deleteActivity(Long id, User user) {
        return activityRepository.findByIdAndCreatorAndDateTimeGreaterThanEqual(id, user, LocalDateTime.now())
            .map(this::deleteActivity)
            .orElseThrow(EntityNotFoundException::new);
    }

    private Boolean deleteActivity(Activity activity) {
        activityRepository.delete(activity);
        log.info("ACTIVITY {} deleted", activity.getId());
        return TRUE;
    }
}
