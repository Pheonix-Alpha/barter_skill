package com.skillexchange.service;

import com.skillexchange.model.*;
import com.skillexchange.repository.LessonRepository;
import com.skillexchange.repository.SkillExchangeRequestRepository;
import com.skillexchange.repository.SkillRepository;
import com.skillexchange.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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

    /**
     * Schedule a new lesson only if an accepted exchange request exists.
     */
    public Lesson scheduleLesson(Long senderId, Long receiverId, Long skillId, LocalDateTime time, int duration, String notes) {
        if (time.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Scheduled time must be in the future.");
        }

        if (duration < 15 || duration > 180) {
            throw new IllegalArgumentException("Duration must be between 15 and 180 minutes.");
        }

        User sender = userRepo.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        User receiver = userRepo.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        Skill skill = skillRepo.findById(skillId)
                .orElseThrow(() -> new RuntimeException("Skill not found"));

        // Check if an accepted exchange request exists
       boolean hasAccepted = requestRepo.existsAcceptedExchangeBetweenUsersAndSkill(senderId, receiverId, skillId);

        if (!hasAccepted) {
            throw new IllegalStateException("You must have an accepted exchange request before scheduling a lesson.");
        }

        // Prevent duplicate scheduling
        boolean alreadyScheduled = lessonRepo.existsBySenderIdAndReceiverIdAndSkillIdAndScheduledTime(
                senderId, receiverId, skillId, time
        );
        if (alreadyScheduled) {
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
        lesson.setPlatformLink(null); // Optional; can be added later

        return lessonRepo.save(lesson);
    }

    public List<Lesson> getMyLessons() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return lessonRepo.findBySenderIdOrReceiverId(user.getId(), user.getId());
    }

    public Lesson updateStatus(Long lessonId, LessonStatus status) {
        Lesson lesson = lessonRepo.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));
        lesson.setStatus(status);
        return lessonRepo.save(lesson);
    }

    public Lesson updatePlatformLink(Long lessonId, String platformLink) {
        Lesson lesson = lessonRepo.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));
        lesson.setPlatformLink(platformLink);
        return lessonRepo.save(lesson);
    }

    public Lesson updateNotes(Long lessonId, String notes) {
        Lesson lesson = lessonRepo.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));
        lesson.setNotes(notes);
        return lessonRepo.save(lesson);
    }

    public boolean lessonAlreadyScheduled(Long userAId, Long userBId, Long skillId) {
        return lessonRepo.existsByParticipantsAndSkill(userAId, userBId, skillId);
    }
}
