package com.milo.controller;

import com.milo.model.User;
import com.milo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, Object> payload) {
        String email = (String) payload.get("email");
        String name = (String) payload.get("name");
        String password = (String) payload.get("password");
        String college = (String) payload.get("college");
        String location = (String) payload.get("location");

        if (userRepository.existsByEmail(email)) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email is already registered"));
        }

        User user = User.builder()
                .name(name != null ? name : "New User")
                .email(email)
                .passwordHash("$2a$10$e7xV6.bSg/yP/hXWq0Vw5e7yX.J3p9M5b7V") // Simulated hash for demo
                .role(User.Role.USER)
                .college(college != null ? college : "Pune College")
                .location(location != null ? location : "Wakad, Pune")
                .bio("Excited to explore activities and meet new friends in Pune!")
                .photoUrl("https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&q=80&w=400")
                .status(User.UserStatus.ACTIVE)
                .interests(Set.of("Coffee", "Gaming", "Trekking"))
                .build();

        User saved = userRepository.save(user);
        return ResponseEntity.ok(Map.of(
            "message", "User registered successfully",
            "token", "mock-jwt-token-" + saved.getId(),
            "user", saved
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid email or password"));
        }

        User user = userOpt.get();
        if (user.getStatus() == User.UserStatus.BLOCKED) {
            return ResponseEntity.status(403).body(Map.of("message", "Account has been blocked by Admin. Contact support."));
        }

        return ResponseEntity.ok(Map.of(
            "token", "mock-jwt-token-" + user.getId(),
            "user", user
        ));
    }
}
