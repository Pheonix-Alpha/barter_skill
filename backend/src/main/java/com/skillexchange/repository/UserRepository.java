package com.skillexchange.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.skillexchange.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    // Default basic queries
     @EntityGraph(attributePaths = {"sentRequests", "receivedRequests", "friends"})
    Optional<User> findByEmail(String email);

    // ✅ Modified version with EntityGraph to eagerly fetch relationships
   
    Optional<User> findByUsername(String username);
}
