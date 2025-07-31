package com.skillexchange.controller;


import com.skillexchange.dto.LessonRequestDto;
import com.skillexchange.dto.LessonResponseDto;
import com.skillexchange.model.*;
import com.skillexchange.repository.SkillExchangeRequestRepository;
import com.skillexchange.repository.UserRepository;
import com.skillexchange.service.LessonService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/lessons")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class LessonController {


    @Autowired
    private LessonService lessonService;

    @Autowired
    private SkillExchangeRequestRepository skillExchangeRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/schedule")
    public ResponseEntity<?> scheduleLesson(@RequestBody LessonRequestDto dto) {
        System.out.println("➡️ Incoming lesson request: " + dto);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User sender = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

      log.info("➡️ Incoming lesson request: {}", dto);
log.info("🔐 Authenticated user: {}", SecurityContextHolder.getContext().getAuthentication());

        

        try {
            Lesson lesson = lessonService.scheduleLesson(
                sender.getId(),
                dto.getReceiverId(),
                dto.getSkillId(),
                dto.getTime(),
                dto.getDuration(),
                dto.getNotes()
            );

            return ResponseEntity.ok(LessonMapper.toDto(lesson));

        } catch (IllegalStateException ex) {
            // For business logic issues like "already scheduled"
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        } catch (Exception ex) {
            // Catch-all for any unexpected issues
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Something went wrong: " + ex.getMessage()));
        }
    }

    @GetMapping
    public List<LessonResponseDto> getMyLessons() {
        return lessonService.getMyLessons().stream()
                .map(LessonMapper::toDto)
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}/status")
    public LessonResponseDto updateStatus(@PathVariable Long id, @RequestParam LessonStatus status) {
        Lesson lesson = lessonService.updateStatus(id, status);
        return LessonMapper.toDto(lesson);
    }

    @GetMapping("/has-accepted")
    public ResponseEntity<Map<String, Boolean>> checkIfAcceptedExchangeExists(
            @RequestParam Long userId,
            @RequestParam Long skillId
    ) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean allowed = skillExchangeRequestRepository
        .existsAcceptedExchangeBetweenUsersAndSkill(currentUser.getId(), userId, skillId);

        return ResponseEntity.ok(Map.of("allowed", allowed));
    }

 @GetMapping("/accepted-requests")
public List<Map<String, Object>> getAcceptedRequestsForScheduling() {
    String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
    User currentUser = userRepository.findByUsername(currentUsername)
            .orElseThrow(() -> new RuntimeException("User not found"));

    List<SkillExchangeRequest> acceptedRequests = skillExchangeRequestRepository
            .findByStatusAndSenderOrReceiver(RequestStatus.ACCEPTED, currentUser.getId());

    Map<String, Map<String, Object>> uniqueRequests = new java.util.HashMap<>();

    for (SkillExchangeRequest req : acceptedRequests) {
        User requester = req.getRequester();
        User target = req.getTarget();
        User otherUser = requester.getId().equals(currentUser.getId()) ? target : requester;

        // Determine skill and skillId
        Skill skill = (req.getType() == SkillType.WANTED) ? req.getWantedSkill() : req.getOfferedSkill();

        if (skill == null) continue; // Skip if skill is not set

        String uniqueKey = otherUser.getId() + "-" + skill.getId();

        if (!uniqueRequests.containsKey(uniqueKey)) {
            boolean lessonScheduled = lessonService.lessonAlreadyScheduled(
                    currentUser.getId(), otherUser.getId(), skill.getId()
            );

            Map<String, Object> map = new java.util.HashMap<>();
            map.put("id", req.getId());
            map.put("skillId", skill.getId());
            map.put("skillName", skill.getName());
            map.put("requesterId", requester.getId());
            map.put("requesterUsername", requester.getUsername());
            map.put("targetId", target.getId());
            map.put("targetUsername", target.getUsername());
            map.put("userId", otherUser.getId());
            map.put("username", otherUser.getUsername());
           map.put("lessonScheduled", lessonScheduled);

if (lessonScheduled) {
    lessonService.getScheduledLesson(currentUser.getId(), otherUser.getId(), skill.getId())
        .ifPresent(lesson -> {
            map.put("platformLink", lesson.getPlatformLink());  // ✅ Zoom link
        });
}


            uniqueRequests.put(uniqueKey, map);
        }
    }

    return new java.util.ArrayList<>(uniqueRequests.values());
}


}
