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
    
   boolean existsByRequesterAndTargetAndWantedSkillAndStatus(User requester, User target, Skill wantedSkill, RequestStatus status);

  @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END " +
       "FROM SkillExchangeRequest r " +
       "WHERE r.status = 'ACCEPTED' AND " +
       "((r.requester.id = :user1 AND r.target.id = :user2) OR " +
       "(r.requester.id = :user2 AND r.target.id = :user1)) AND " +
       "(r.wantedSkill.id = :skillId OR r.offeredSkill.id = :skillId)")
boolean existsAcceptedExchangeBetweenUsersAndSkill(
        @Param("user1") Long user1,
        @Param("user2") Long user2,
        @Param("skillId") Long skillId);

        
@Query("SELECT r FROM SkillExchangeRequest r " +
       "JOIN FETCH r.requester " +
       "JOIN FETCH r.target " +
       "LEFT JOIN FETCH r.wantedSkill " +
       "LEFT JOIN FETCH r.offeredSkill " +
       "WHERE r.requester.id = :userId OR r.target.id = :userId")
List<SkillExchangeRequest> findByUserWithDetails(@Param("userId") Long userId);


    @Query("SELECT r FROM SkillExchangeRequest r " +
       "WHERE r.status = :status AND (r.requester.id = :userId OR r.target.id = :userId)")
List<SkillExchangeRequest> findByStatusAndSenderOrReceiver(
        @Param("status") RequestStatus status,
        @Param("userId") Long userId);




        

}
