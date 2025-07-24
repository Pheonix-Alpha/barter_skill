package com.skillexchange.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.skillexchange.model.User;
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    @EntityGraph(attributePaths = {"friends", "sentRequests", "receivedRequests"})
    Optional<User> findWithRelationsByUsername(String username);

    @EntityGraph(attributePaths = {"friends", "sentRequests", "receivedRequests"})
    Optional<User> findWithRelationsById(Long id);

    // New methods
    @Query("SELECT DISTINCT u FROM User u " +
           "JOIN u.userSkills us " +
           "JOIN us.skill s " +
           "WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :skillName, '%')) " +
           "AND us.type = :type")
    List<User> findBySkillNameAndType(@Param("skillName") String skillName,
                                      @Param("type") com.skillexchange.model.SkillType type);
}
