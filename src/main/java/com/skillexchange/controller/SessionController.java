package com.skillexchange.controller;

import com.skillexchange.model.LearningSession;
import com.skillexchange.model.User;
import com.skillexchange.repository.LearningSessionRepository;
import com.skillexchange.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    private final LearningSessionRepository sessionRepo;
    private final UserRepository userRepo;

    public SessionController(LearningSessionRepository sessionRepo, UserRepository userRepo) {
        this.sessionRepo = sessionRepo;
        this.userRepo = userRepo;
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepo.findByUsername(username).orElseThrow();
    }

    @PostMapping("/schedule/{teacherId}")
    public LearningSession schedule(@PathVariable Long teacherId, @RequestParam String topic, @RequestParam String time) {
        User learner = getCurrentUser();
        User teacher = userRepo.findById(teacherId).orElseThrow();

        LocalDateTime scheduledAt = LocalDateTime.parse(time); // ISO format: 2025-07-04T17:30

        LearningSession session = LearningSession.builder()
                .learner(learner)
                .teacher(teacher)
                .topic(topic)
                .scheduledAt(scheduledAt)
                .build();

        return sessionRepo.save(session);
    }

    @GetMapping("/my")
    public List<LearningSession> mySessions() {
        User me = getCurrentUser();
        return sessionRepo.findByLearnerOrTeacher(me, me);
    }
}
