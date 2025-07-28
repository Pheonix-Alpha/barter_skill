package com.skillexchange.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LessonRequestDto {
    private Long receiverId;
    private Long skillId;
    private LocalDateTime time;
    private int duration;
    private String notes;

    @Override
    public String toString() {
        return "LessonRequestDto{" +
                "receiverId=" + receiverId +
                ", skillId=" + skillId +
                ", time=" + time +
                ", duration=" + duration +
                ", notes='" + notes + '\'' +
                '}';
    }
}
