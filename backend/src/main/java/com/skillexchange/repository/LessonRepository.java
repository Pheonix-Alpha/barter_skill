package com.skillexchange.repository;

import com.skillexchange.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LessonRepository extends JpaRepository<Lesson, Long> {

    List<Lesson> findBySenderIdOrReceiverId(Long senderId, Long receiverId);

    boolean existsBySenderIdAndReceiverIdAndSkillIdAndScheduledTime(
            Long senderId, Long receiverId, Long skillId, LocalDateTime scheduledTime
    );

    List<Lesson> findByScheduledTimeBetweenAndPlatformLinkIsNull(LocalDateTime start, LocalDateTime end);

    // ✅ FIXED: Correct way to query by sender/receiver + skill (bi-directional)
    @Query("SELECT l FROM Lesson l WHERE " +
           "((l.sender.id = :userA AND l.receiver.id = :userB) OR " +
           "(l.sender.id = :userB AND l.receiver.id = :userA)) AND " +
           "l.skill.id = :skillId")
    Optional<Lesson> findByParticipantsAndSkill(@Param("userA") Long userAId,
                                                @Param("userB") Long userBId,
                                                @Param("skillId") Long skillId);

    @Query("SELECT COUNT(l) > 0 FROM Lesson l WHERE " +
           "((l.sender.id = :userA AND l.receiver.id = :userB) OR " +
           "(l.sender.id = :userB AND l.receiver.id = :userA)) AND " +
           "l.skill.id = :skillId")
    boolean existsByParticipantsAndSkill(@Param("userA") Long userAId,
                                         @Param("userB") Long userBId,
                                         @Param("skillId") Long skillId);
}
