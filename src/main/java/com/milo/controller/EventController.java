package com.milo.controller;

import com.milo.model.Event;
import com.milo.model.User;
import com.milo.repository.EventRepository;
import com.milo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/events")
@CrossOrigin(origins = "*")
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEventById(@PathVariable UUID id) {
        return eventRepository.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createEvent(@RequestBody Event event) {
        if (event.getStatus() == null) event.setStatus(Event.EventStatus.APPROVED);
        Event saved = eventRepository.save(event);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/{id}/join")
    public ResponseEntity<?> joinEvent(@PathVariable UUID id, @RequestParam UUID userId) {
        return eventRepository.findById(id).map(event -> {
            if (event.getJoinedSpots() < event.getTotalSpots()) {
                event.setJoinedSpots(event.getJoinedSpots() + 1);
                Event updated = eventRepository.save(event);
                return ResponseEntity.ok(Map.of("message", "Joined event successfully", "event", updated));
            } else {
                return ResponseEntity.badRequest().body(Map.of("message", "Event is already full"));
            }
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/leave")
    public ResponseEntity<?> leaveEvent(@PathVariable UUID id, @RequestParam UUID userId) {
        return eventRepository.findById(id).map(event -> {
            event.setJoinedSpots(Math.max(0, event.getJoinedSpots() - 1));
            Event updated = eventRepository.save(event);
            return ResponseEntity.ok(Map.of("message", "Left event successfully", "event", updated));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}/plans")
    public List<Event> getMyPlans(@PathVariable UUID userId) {
        return eventRepository.findByCreatedById(userId);
    }
}
