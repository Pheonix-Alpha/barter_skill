package com.skillexchange.service;

import com.skillexchange.model.*;
import com.skillexchange.repository.LessonRepository;
import com.skillexchange.repository.SkillExchangeRequestRepository;
import com.skillexchange.repository.SkillRepository;
import com.skillexchange.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class LessonService {

    @Autowired
    private LessonRepository lessonRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private SkillRepository skillRepo;

    @Autowired
    private SkillExchangeRequestRepository requestRepo;

    @Autowired
    private ZoomService zoomService;

    /**
     * Schedule a new lesson only if an accepted exchange request exists.
     */
    public Lesson scheduleLesson(Long senderId, Long receiverId, Long skillId, LocalDateTime time, int duration, String notes) {
        log.info("📅 Attempting to schedule lesson: sender={}, receiver={}, skill={}, time={}, duration={}", senderId, receiverId, skillId, time, duration);

        if (time.isBefore(LocalDateTime.now())) {
            log.warn("⛔ Rejected scheduling: Time is in the past");
            throw new IllegalArgumentException("Scheduled time must be in the future.");
        }

        if (duration < 15 || duration > 180) {
            log.warn("⛔ Rejected scheduling: Invalid duration {}", duration);
            throw new IllegalArgumentException("Duration must be between 15 and 180 minutes.");
        }

        User sender = userRepo.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepo.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));
        Skill skill = skillRepo.findById(skillId)
                .orElseThrow(() -> new RuntimeException("Skill not found"));

        boolean hasAccepted = requestRepo.existsAcceptedExchangeBetweenUsersAndSkill(senderId, receiverId, skillId);

        if (!hasAccepted) {
            log.warn("⛔ Cannot schedule: No accepted exchange request exists between {} and {} for skill {}", senderId, receiverId, skillId);
            throw new IllegalStateException("You must have an accepted exchange request before scheduling a lesson.");
        }

        boolean alreadyScheduled = lessonRepo.existsBySenderIdAndReceiverIdAndSkillIdAndScheduledTime(senderId, receiverId, skillId, time);
        if (alreadyScheduled) {
            log.warn("⚠️ Duplicate scheduling prevented for lesson with sender={}, receiver={}, skill={}, time={}", senderId, receiverId, skillId, time);
            throw new IllegalStateException("Lesson already scheduled at this time with this user for this skill.");
        }

        Lesson lesson = new Lesson();
        lesson.setSender(sender);
        lesson.setReceiver(receiver);
        lesson.setSkill(skill);
        lesson.setScheduledTime(time);
        lesson.setDurationMinutes(duration);
        lesson.setStatus(LessonStatus.PENDING);
        lesson.setNotes(notes);

        String isoTime = time.atZone(java.time.ZoneId.systemDefault()).toInstant().toString();

        try {
            String zoomLink = zoomService.createMeeting("Skill Exchange Lesson", isoTime, duration);
            lesson.setPlatformLink(zoomLink);
            log.info("✅ Zoom link created for lesson: {}", zoomLink);
        } catch (Exception e) {
            log.error("❌ Failed to create Zoom link: {}", e.getMessage(), e);
        }

        Lesson saved = lessonRepo.save(lesson);
        log.info("💾 Lesson saved with ID: {}", saved.getId());
        return saved;
    }

    public List<Lesson> getMyLessons() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("👤 Fetching lessons for user: {}", username);

        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Lesson> lessons = lessonRepo.findBySenderIdOrReceiverId(user.getId(), user.getId());
        log.info("📚 Found {} lessons for user {}", lessons.size(), user.getId());
        return lessons;
    }

    public Lesson updateStatus(Long lessonId, LessonStatus status) {
        log.info("🔄 Updating lesson status: lessonId={}, newStatus={}", lessonId, status);

        Lesson lesson = lessonRepo.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        lesson.setStatus(status);
        return lessonRepo.save(lesson);
    }

    public Lesson updatePlatformLink(Long lessonId, String platformLink) {
        log.info("🔗 Updating platform link for lessonId={}", lessonId);

        Lesson lesson = lessonRepo.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        lesson.setPlatformLink(platformLink);
        return lessonRepo.save(lesson);
    }

    public Lesson updateNotes(Long lessonId, String notes) {
        log.info("📝 Updating notes for lessonId={}", lessonId);

        Lesson lesson = lessonRepo.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        lesson.setNotes(notes);
        return lessonRepo.save(lesson);
    }

    public boolean lessonAlreadyScheduled(Long userAId, Long userBId, Long skillId) {
        boolean exists = lessonRepo.existsByParticipantsAndSkill(userAId, userBId, skillId);
        log.debug("📌 Checking if lesson already scheduled between {} and {} for skill {}: {}", userAId, userBId, skillId, exists);
        return exists;
    }

    public Optional<Lesson> getScheduledLesson(Long userAId, Long userBId, Long skillId) {
        log.debug("🔍 Getting scheduled lesson between {} and {} for skill {}", userAId, userBId, skillId);
        return lessonRepo.findByParticipantsAndSkill(userAId, userBId, skillId);
    }
}
