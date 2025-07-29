package com.skillexchange.dto;

import com.skillexchange.model.LearningSession;
import com.skillexchange.model.User;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LearningSessionDto {
    private Long id;
    private String topic;
    private String partnerName;
    private LocalDateTime scheduledAt;

    public static LearningSessionDto from(LearningSession session, User currentUser) {
        String partner = session.getLearner().equals(currentUser)
                ? session.getTeacher().getUsername()
                : session.getLearner().getUsername();

        return new LearningSessionDto(
                session.getId(),
                session.getTopic(),
                partner,
                session.getScheduledAt()
        );
    }
}
