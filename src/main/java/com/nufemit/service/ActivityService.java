package com.nufemit.service;

import com.nufemit.model.Activity;
import com.nufemit.model.User;
import com.nufemit.model.dto.ActivityDTO;
import com.nufemit.repository.ActivityRepository;
import com.nufemit.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Boolean.TRUE;

@Service
@AllArgsConstructor
@Slf4j
public class ActivityService {

    private ActivityRepository activityRepository;
    private UserRepository userRepository;

    private static ActivityDTO mapToSimpleActivityDTO(Activity activity) {
        return ActivityDTO.builder()
            .title(activity.getTitle())
            .activityImage(activity.getActivityImage())
            .description(activity.getDescription())
            .place(activity.getPlace())
            .dateTime(activity.getDateTime())
            .id(activity.getId())
            .completed(activity.getParticipants().size() == activity.getMaxParticipants())
            .build();
    }

    public Boolean createActivity(Activity activity, User user) {
        activity.setCreator(user);
        if (activity.getMaxParticipants() == null) activity.setMaxParticipants(0);
        if (activity.getDateTime() == null) activity.setDateTime(LocalDateTime.now());
        if (activity.getPrice() == null) activity.setPrice(0d);
        Activity newActivity = activityRepository.save(activity);
        log.info("New ACTIVITY created: {}", newActivity.getId());
        return TRUE;
    }

    public List<Activity> getRecentActivities(User user) {
        log.info("Fetching recent activities");
        return activityRepository.findTop5ByParticipantsContainsAndDateTimeLessThanOrderByDateTimeDesc(user, LocalDateTime.now());
    }

    public List<Activity> getRecentActivitiesForUser(Long id) {
        log.info("Fetching recent activities");
        return userRepository.findById(id)
            .map(user -> activityRepository.findTop5ByParticipantsContainsAndDateTimeLessThanOrderByDateTimeDesc(user, LocalDateTime.now()))
            .orElseThrow(EntityNotFoundException::new);
    }

    public ActivityDTO getActivitiesById(Long id, User user) {
        log.info("Fetching activity by id {}", id);
        return activityRepository.findById(id)
            .map(activity -> mapToActivityDTO(activity, user))
            .orElseThrow(EntityNotFoundException::new);
    }

    public Boolean deleteActivity(Long id, User user) {
        log.info("Deleting activity by id {}", id);
        return activityRepository.findByIdAndCreatorAndDateTimeGreaterThanEqual(id, user, LocalDateTime.now())
            .map(this::deleteActivity)
            .orElseThrow(EntityNotFoundException::new);
    }

    public void deleteAllActivitiesForCreator(User user) {
        log.info("Deleting all activities for creator id {}", user.getId());
        activityRepository.deleteAllByCreator(user);
    }

    private Boolean deleteActivity(Activity activity) {
        activityRepository.delete(activity);
        log.info("ACTIVITY {} deleted", activity.getId());
        return TRUE;
    }

    public List<ActivityDTO> getActivities(String searchBox) {
        log.info("Fetching activities");
        if (searchBox == null || searchBox.isBlank()) {
            return activityRepository.findTop25ByDateTimeGreaterThanEqualOrderByDateTimeDesc(LocalDateTime.now()).stream()
                .map(ActivityService::mapToSimpleActivityDTO)
                .collect(Collectors.toList());
        }
        return activityRepository.findBySearchBox(LocalDateTime.now(), searchBox, searchBox, searchBox).stream()
            .map(ActivityService::mapToSimpleActivityDTO)
            .collect(Collectors.toList());
    }

    private ActivityDTO mapToActivityDTO(Activity activity, User user) {
        return ActivityDTO.builder()
            .id(activity.getId())
            .title(activity.getTitle())
            .shortDescription(activity.getDescription().length() > 160 ?
                activity.getDescription().substring(0, 157).concat("...") :
                activity.getDescription())
            .description(activity.getDescription())
            .place(activity.getPlace())
            .dateTime(activity.getDateTime())
            .price(activity.getPrice())
            .joined(activity.getParticipants().contains(user))
            .limit(activity.getMaxParticipants())
            .joiners(activity.getParticipants())
            .activityImage(activity.getActivityImage())
            .creator(activity.getCreator())
            .build();
    }
}
