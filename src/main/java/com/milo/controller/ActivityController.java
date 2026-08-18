package com.milo.controller;

import com.milo.model.Activity;
import com.milo.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/activities")
@CrossOrigin(origins = "*")
public class ActivityController {

    @Autowired
    private ActivityRepository activityRepository;

    @GetMapping
    public List<Activity> getAllActivities() {
        return activityRepository.findAll();
    }

    @GetMapping("/category/{category}")
    public List<Activity> getActivitiesByCategory(@PathVariable String category) {
        return activityRepository.findByCategoryIgnoreCase(category);
    }
}
