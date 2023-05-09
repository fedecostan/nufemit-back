package com.nufemit.repository;

import com.nufemit.model.Activity;
import com.nufemit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    @Query("SELECT a FROM Activity a WHERE a.dateTime >= ?1 and" +
        " (a.title like CONCAT('%', ?2, '%') or" +
        " a.description like CONCAT('%', ?3, '%') or" +
        " a.place like CONCAT('%', ?4, '%'))")
    List<Activity> findBySearchBox(LocalDateTime now, String title, String description, String place);

    List<Activity> findTop25ByDateTimeGreaterThanEqualOrderByDateTimeDesc(LocalDateTime now);

    List<Activity> findTop5ByParticipantsContainsAndDateTimeLessThanOrderByDateTimeDesc(User user, LocalDateTime now);

    Optional<Activity> findByIdAndDateTimeGreaterThanEqual(Long activityId, LocalDateTime now);

    Optional<Activity> findByIdAndCreatorAndDateTimeGreaterThanEqual(Long id, User user, LocalDateTime now);

    List<Activity> findByParticipantsContains(User user);

    void deleteAllByCreator(User user);
}
