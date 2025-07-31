package com.skillexchange.service;

import com.skillexchange.model.Lesson;
import com.skillexchange.repository.LessonRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Slf4j
@Service
public class LessonSchedulerService {

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private ZoomService zoomService;

    /**
     * Every minute, check if any lesson is due within ±1 minute and has no Zoom link.
     */
    @Scheduled(fixedRate = 60000) // Run every 60 seconds
    @Transactional
    public void assignZoomLinksToDueLessons() {
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime start = now.minusMinutes(1);
        LocalDateTime end = now.plusMinutes(1);

        log.info("🕒 Scheduler running at {}, checking for lessons between {} and {}", now, start, end);

        List<Lesson> dueLessons = lessonRepository.findByScheduledTimeBetweenAndPlatformLinkIsNull(start, end);
        log.info("📋 Found {} lessons without Zoom link", dueLessons.size());

        for (Lesson lesson : dueLessons) {
            try {
                log.info("🔧 Generating Zoom link for lessonId={} (skill={}, sender={}, receiver={}, time={})",
                        lesson.getId(),
                        lesson.getSkill().getName(),
                        lesson.getSender().getUsername(),
                        lesson.getReceiver().getUsername(),
                        lesson.getScheduledTime());

                String zoomLink = zoomService.createMeetingLink(
                        lesson.getSender().getUsername(),
                        lesson.getScheduledTime(),
                        lesson.getDurationMinutes()
                );

                lesson.setPlatformLink(zoomLink);
                lessonRepository.save(lesson);

                log.info("✅ Zoom link created and saved for lessonId={}: {}", lesson.getId(), zoomLink);
            } catch (Exception e) {
                log.error("❌ Failed to create Zoom link for lessonId={}: {}", lesson.getId(), e.getMessage(), e);
            }
        }
    }
}
