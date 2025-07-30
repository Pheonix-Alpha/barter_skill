package com.skillexchange.repository;

import com.skillexchange.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {

    List<Lesson> findBySenderIdOrReceiverId(Long senderId, Long receiverId);

    boolean existsBySenderIdAndReceiverIdAndSkillIdAndScheduledTime(
            Long senderId, Long receiverId, Long skillId, LocalDateTime scheduledTime);

    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END FROM Lesson l " +
           "WHERE l.skill.id = :skillId AND " +
           "((l.sender.id = :userA AND l.receiver.id = :userB) " +
           "OR (l.sender.id = :userB AND l.receiver.id = :userA))")
    boolean existsByParticipantsAndSkill(@Param("userA") Long userAId,
                                         @Param("userB") Long userBId,
                                         @Param("skillId") Long skillId);
}
