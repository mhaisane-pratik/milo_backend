package com.milo.repository;

import com.milo.model.Connection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, UUID> {
    List<Connection> findByRequesterIdOrReceiverId(UUID requesterId, UUID receiverId);
    Optional<Connection> findByRequesterIdAndReceiverId(UUID requesterId, UUID receiverId);
}
