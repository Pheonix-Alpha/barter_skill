package com.skillexchange.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.skillexchange.model.User;
import com.skillexchange.model.SkillType;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    // ✅ Load profile + skills + friends + requests (for current user)
    @EntityGraph(attributePaths = {
        "friends",
        "sentRequests",
        "receivedRequests",
        "userSkills.skill"
    })
    Optional<User> findWithRelationsByUsername(String username);

    // ✅ Same for lookup by ID
    @EntityGraph(attributePaths = {
        "friends",
        "sentRequests",
        "receivedRequests",
        "userSkills.skill"
    })
    Optional<User> findWithRelationsById(Long id);

    // ✅ Search by skill name and type (OFFERED or WANTED)
  @Query("SELECT DISTINCT u FROM User u " +
       "JOIN FETCH u.userSkills us " +
       "JOIN FETCH us.skill s " +
       "WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :skillName, '%')) " +
       "AND us.type = :type")

    List<User> findBySkillNameAndType(@Param("skillName") String skillName,
                                      @Param("type") SkillType type);

    @Query("SELECT DISTINCT u FROM User u " +
       "LEFT JOIN FETCH u.userSkills us " +
       "LEFT JOIN FETCH us.skill " +
       "WHERE u IN :users")
List<User> findAllWithSkills(@Param("users") List<User> users);

}
