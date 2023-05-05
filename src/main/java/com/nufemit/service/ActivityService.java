package com.nufemit.service;

import com.nufemit.model.Activity;
import com.nufemit.repository.ActivityRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static java.lang.Boolean.TRUE;

@Service
@AllArgsConstructor
@Slf4j
public class ActivityService {

    private ActivityRepository activityRepository;

    public Boolean createActivity(Activity activity) {
        activityRepository.save(activity);
        return TRUE;
    }
}
