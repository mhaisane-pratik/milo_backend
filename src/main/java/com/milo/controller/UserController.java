package com.milo.controller;

import com.milo.model.User;
import com.milo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable UUID id) {
        return userRepository.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/profile")
    public ResponseEntity<?> updateProfile(@PathVariable UUID id, @RequestBody Map<String, Object> payload) {
        return userRepository.findById(id).map(user -> {
            if (payload.containsKey("name")) user.setName((String) payload.get("name"));
            if (payload.containsKey("bio")) user.setBio((String) payload.get("bio"));
            if (payload.containsKey("college")) user.setCollege((String) payload.get("college"));
            if (payload.containsKey("location")) user.setLocation((String) payload.get("location"));
            if (payload.containsKey("photoUrl")) user.setPhotoUrl((String) payload.get("photoUrl"));
            if (payload.containsKey("interests")) {
                List<String> interestsList = (List<String>) payload.get("interests");
                user.setInterests(new HashSet<>(interestsList));
            }
            User updated = userRepository.save(user);
            return ResponseEntity.ok(updated);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
