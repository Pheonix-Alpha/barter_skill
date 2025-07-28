package com.skillexchange.controller;

import com.skillexchange.dto.LessonResponseDto;
import com.skillexchange.model.Lesson;

public class LessonMapper {
    public static LessonResponseDto toDto(Lesson lesson) {
        LessonResponseDto dto = new LessonResponseDto();
        dto.setId(lesson.getId());
        dto.setSenderId(lesson.getSender().getId());
        dto.setSenderUsername(lesson.getSender().getUsername());
        dto.setReceiverId(lesson.getReceiver().getId());
        dto.setReceiverUsername(lesson.getReceiver().getUsername());
        dto.setSkillId(lesson.getSkill().getId());
        dto.setSkillName(lesson.getSkill().getName());
        dto.setScheduledTime(lesson.getScheduledTime());
        dto.setDuration(lesson.getDurationMinutes());
        dto.setStatus(lesson.getStatus());
        dto.setNotes(lesson.getNotes());
        dto.setPlatformLink(lesson.getPlatformLink());
        return dto;
    }
}
