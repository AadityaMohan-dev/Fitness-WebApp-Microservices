package com.fitness.activityservice.sevices;

import com.fitness.activityservice.dto.ActivityRequest;
import com.fitness.activityservice.dto.ActivityResponse;
import com.fitness.activityservice.model.Activity;
import com.fitness.activityservice.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final UserValidationService userValidationService;

    public ActivityResponse trackActivity(ActivityRequest request) {
        log.info("Attempting to track activity for userId: {}", request.getUserId());

        boolean isValidUser = userValidationService.validateUserId(request.getUserId());
        if (!isValidUser) {
            log.warn("Activity tracking failed - invalid userId: {}", request.getUserId());
            throw new RuntimeException("Invalid user id " + request.getUserId());
        }

        Activity activity = Activity.builder()
                .userId(request.getUserId())
                .type(request.getType())
                .duration(request.getDuration())
                .caloriesBurned(request.getCaloriesBurned())
                .startTime(request.getStartTime())
                .additionalMetrics(request.getAdditionalMetrics())
                .build();

        Activity savedActivity = activityRepository.save(activity);
        log.info("Activity tracked successfully with ID: {} for userId: {}", savedActivity.getId(), savedActivity.getUserId());

        return mapToResponse(savedActivity);
    }

    private ActivityResponse mapToResponse(Activity activity) {
        log.trace("Mapping Activity entity to ActivityResponse for activityId: {}", activity.getId());

        ActivityResponse activityResponse = new ActivityResponse();
        activityResponse.setId(activity.getId());
        activityResponse.setUserId(activity.getUserId());
        activityResponse.setType(activity.getType());
        activityResponse.setDuration(activity.getDuration());
        activityResponse.setCaloriesBurned(activity.getCaloriesBurned());
        activityResponse.setStartTime(activity.getStartTime());
        activityResponse.setAdditionalMetrics(activity.getAdditionalMetrics());
        activityResponse.setCreatedAt(activity.getCreatedAt());
        activityResponse.setUpdatedAt(activity.getUpdatedAt());

        return activityResponse;
    }

    public List<ActivityResponse> getUserActivities(String userId) {
        log.info("Fetching activities for userId: {}", userId);

        List<Activity> activities = activityRepository.findByUserId(userId);
        log.debug("Found {} activities for userId: {}", activities.size(), userId);

        return activities.stream().map(this::mapToResponse).toList();
    }

    public ActivityResponse getActivityById(String activityId) {
        log.info("Fetching activity by ID: {}", activityId);

        return activityRepository.findById(activityId)
                .map(activity -> {
                    log.debug("Activity found with ID: {}", activityId);
                    return mapToResponse(activity);
                })
                .orElseThrow(() -> {
                    log.error("Activity not found for ID: {}", activityId);
                    return new RuntimeException("Activity not found");
                });
    }
}
