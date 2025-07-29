
package com.skillexchange.dto;

import java.time.LocalDateTime;

public record ScheduleLessonRequest(
    Long requestId,
    Long teacherId,
    Long learnerId,
    String topic,
    LocalDateTime scheduledAt
) {}
