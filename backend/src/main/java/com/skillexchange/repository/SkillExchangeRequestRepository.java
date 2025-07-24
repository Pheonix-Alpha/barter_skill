package com.skillexchange.repository;

import com.skillexchange.model.RequestStatus;
import com.skillexchange.model.Skill;
import com.skillexchange.model.SkillExchangeRequest;
import com.skillexchange.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SkillExchangeRequestRepository extends JpaRepository<SkillExchangeRequest, Long> {
    List<SkillExchangeRequest> findByRequester(User user);
    List<SkillExchangeRequest> findByTarget(User user);
    
    boolean existsByRequesterAndTargetAndSkillAndStatus(
        User requester,
        User target,
        Skill skill,
        RequestStatus status
    );
     // ✅ Add this to fetch all relevant data eagerly
    @Query("SELECT r FROM SkillExchangeRequest r " +
           "JOIN FETCH r.requester " +
           "JOIN FETCH r.target " +
           "JOIN FETCH r.skill " +
           "WHERE r.requester.id = :userId OR r.target.id = :userId")
    List<SkillExchangeRequest> findByUserWithDetails(@Param("userId") Long userId);

}
