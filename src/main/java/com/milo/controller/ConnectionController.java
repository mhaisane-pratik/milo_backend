package com.milo.controller;

import com.milo.model.Connection;
import com.milo.model.User;
import com.milo.repository.ConnectionRepository;
import com.milo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/connections")
@CrossOrigin(origins = "*")
public class ConnectionController {

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user/{userId}")
    public List<Connection> getUserConnections(@PathVariable UUID userId) {
        return connectionRepository.findByRequesterIdOrReceiverId(userId, userId);
    }

    @PostMapping("/connect")
    public ResponseEntity<?> connectUser(@RequestParam UUID requesterId, @RequestParam UUID receiverId) {
        Optional<User> reqOpt = userRepository.findById(requesterId);
        Optional<User> recOpt = userRepository.findById(receiverId);

        if (reqOpt.isEmpty() || recOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "User not found"));
        }

        Connection conn = Connection.builder()
                .requester(reqOpt.get())
                .receiver(recOpt.get())
                .status(Connection.ConnectionStatus.CONNECTED)
                .build();

        Connection saved = connectionRepository.save(conn);
        return ResponseEntity.ok(Map.of("message", "Connected successfully", "connection", saved));
    }

    @DeleteMapping("/remove/{connectionId}")
    public ResponseEntity<?> removeConnection(@PathVariable UUID connectionId) {
        connectionRepository.deleteById(connectionId);
        return ResponseEntity.ok(Map.of("message", "Connection removed successfully"));
    }
}
