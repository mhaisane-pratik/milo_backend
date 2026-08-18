package com.milo.controller;

import com.milo.model.*;
import com.milo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired private UserRepository userRepository;
    @Autowired private ActivityRepository activityRepository;
    @Autowired private EventRepository eventRepository;
    @Autowired private GroupRepository groupRepository;
    @Autowired private ReportRepository reportRepository;
    @Autowired private AnnouncementRepository announcementRepository;
    @Autowired private AppSettingRepository appSettingRepository;

    // ==========================================
    // 📊 DASHBOARD METRICS OVERVIEW
    // ==========================================
    @GetMapping("/dashboard/metrics")
    public ResponseEntity<?> getDashboardMetrics() {
        long totalUsers = userRepository.count();
        long totalActivities = activityRepository.count();
        long totalEvents = eventRepository.count();
        long totalGroups = groupRepository.count();
        long openReports = reportRepository.findByStatus(Report.ReportStatus.OPEN).size();

        return ResponseEntity.ok(Map.of(
            "totalUsers", totalUsers,
            "totalActivities", totalActivities,
            "totalEvents", totalEvents,
            "totalGroups", totalGroups,
            "openReports", openReports
        ));
    }

    // ==========================================
    // 👥 USERS MODULE (View / Edit / Block / Delete)
    // ==========================================
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PutMapping("/users/{id}/status")
    public ResponseEntity<?> updateUserStatus(@PathVariable UUID id, @RequestParam User.UserStatus status) {
        return userRepository.findById(id).map(user -> {
            user.setStatus(status);
            User updated = userRepository.save(user);
            return ResponseEntity.ok(Map.of("message", "User status updated to " + status, "user", updated));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID id) {
        userRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
    }

    // ==========================================
    // 🎯 ACTIVITIES MODULE (Create / View / Edit / Delete)
    // ==========================================
    @PostMapping("/activities")
    public Activity createActivity(@RequestBody Activity activity) {
        return activityRepository.save(activity);
    }

    @PutMapping("/activities/{id}")
    public ResponseEntity<?> updateActivity(@PathVariable UUID id, @RequestBody Activity activityDetails) {
        return activityRepository.findById(id).map(act -> {
            act.setTitle(activityDetails.getTitle());
            act.setCategory(activityDetails.getCategory());
            act.setDescription(activityDetails.getDescription());
            act.setIcon(activityDetails.getIcon());
            act.setImageUrl(activityDetails.getImageUrl());
            Activity updated = activityRepository.save(act);
            return ResponseEntity.ok(updated);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/activities/{id}")
    public ResponseEntity<?> deleteActivity(@PathVariable UUID id) {
        activityRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Activity deleted successfully"));
    }

    // ==========================================
    // 📅 EVENTS MODULE (Create / View / Edit / Delete)
    // ==========================================
    @PostMapping("/events")
    public Event createEvent(@RequestBody Event event) {
        return eventRepository.save(event);
    }

    @PutMapping("/events/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable UUID id, @RequestBody Event eventDetails) {
        return eventRepository.findById(id).map(evt -> {
            evt.setTitle(eventDetails.getTitle());
            evt.setDescription(eventDetails.getDescription());
            evt.setLocation(eventDetails.getLocation());
            evt.setArea(eventDetails.getArea());
            evt.setEventDate(eventDetails.getEventDate());
            evt.setEventTime(eventDetails.getEventTime());
            evt.setTotalSpots(eventDetails.getTotalSpots());
            evt.setStatus(eventDetails.getStatus());
            evt.setIsFeatured(eventDetails.getIsFeatured());
            Event updated = eventRepository.save(evt);
            return ResponseEntity.ok(updated);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/events/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable UUID id) {
        eventRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Event deleted successfully"));
    }

    // ==========================================
    // 👨‍👩‍👧 GROUPS MODULE (View / Edit / Delete)
    // ==========================================
    @PutMapping("/groups/{id}")
    public ResponseEntity<?> updateGroup(@PathVariable UUID id, @RequestBody Group groupDetails) {
        return groupRepository.findById(id).map(grp -> {
            grp.setName(groupDetails.getName());
            grp.setDescription(groupDetails.getDescription());
            grp.setImageUrl(groupDetails.getImageUrl());
            Group updated = groupRepository.save(grp);
            return ResponseEntity.ok(updated);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/groups/{id}")
    public ResponseEntity<?> deleteGroup(@PathVariable UUID id) {
        groupRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Group deleted successfully"));
    }

    // ==========================================
    // 🚩 REPORTS MODULE (View / Resolve / Delete)
    // ==========================================
    @GetMapping("/reports")
    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    @PutMapping("/reports/{id}/resolve")
    public ResponseEntity<?> resolveReport(@PathVariable UUID id) {
        return reportRepository.findById(id).map(rep -> {
            rep.setStatus(Report.ReportStatus.RESOLVED);
            Report updated = reportRepository.save(rep);
            return ResponseEntity.ok(Map.of("message", "Report resolved successfully", "report", updated));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/reports/{id}")
    public ResponseEntity<?> deleteReport(@PathVariable UUID id) {
        reportRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Report deleted successfully"));
    }

    // ==========================================
    // 📢 ANNOUNCEMENTS MODULE (Create / Edit / Delete)
    // ==========================================
    @GetMapping("/announcements")
    public List<Announcement> getAllAnnouncements() {
        return announcementRepository.findAll();
    }

    @PostMapping("/announcements")
    public Announcement createAnnouncement(@RequestBody Announcement announcement) {
        return announcementRepository.save(announcement);
    }

    @PutMapping("/announcements/{id}")
    public ResponseEntity<?> updateAnnouncement(@PathVariable UUID id, @RequestBody Announcement announcementDetails) {
        return announcementRepository.findById(id).map(anc -> {
            anc.setTitle(announcementDetails.getTitle());
            anc.setContent(announcementDetails.getContent());
            Announcement updated = announcementRepository.save(anc);
            return ResponseEntity.ok(updated);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/announcements/{id}")
    public ResponseEntity<?> deleteAnnouncement(@PathVariable UUID id) {
        announcementRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Announcement deleted successfully"));
    }

    // ==========================================
    // ⚙️ SETTINGS MODULE (View / Update)
    // ==========================================
    @GetMapping("/settings")
    public List<AppSetting> getAppSettings() {
        return appSettingRepository.findAll();
    }

    @PostMapping("/settings")
    public AppSetting updateAppSetting(@RequestBody AppSetting setting) {
        return appSettingRepository.save(setting);
    }
}
