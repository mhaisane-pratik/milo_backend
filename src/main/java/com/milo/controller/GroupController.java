package com.milo.controller;

import com.milo.model.Group;
import com.milo.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/groups")
@CrossOrigin(origins = "*")
public class GroupController {

    @Autowired
    private GroupRepository groupRepository;

    @GetMapping
    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getGroupById(@PathVariable UUID id) {
        return groupRepository.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/join")
    public ResponseEntity<?> joinGroup(@PathVariable UUID id, @RequestParam UUID userId) {
        return ResponseEntity.ok(Map.of("message", "Joined group successfully"));
    }

    @PostMapping("/{id}/leave")
    public ResponseEntity<?> leaveGroup(@PathVariable UUID id, @RequestParam UUID userId) {
        return ResponseEntity.ok(Map.of("message", "Left group successfully"));
    }
}
