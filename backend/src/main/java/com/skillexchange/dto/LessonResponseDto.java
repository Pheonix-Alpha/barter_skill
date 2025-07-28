package com.skillexchange.dto;

import com.skillexchange.model.LessonStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LessonResponseDto {
    private Long id;
    private Long senderId;
    private String senderUsername;

    private Long receiverId;
    private String receiverUsername;

    private Long skillId;
    private String skillName;

    private LocalDateTime scheduledTime;
    private int duration;
    private LessonStatus status;
    private String notes;
    private String platformLink;
}
