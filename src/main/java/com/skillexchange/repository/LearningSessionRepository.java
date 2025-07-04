package com.skillexchange.repository;

import com.skillexchange.model.LearningSession;
import com.skillexchange.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LearningSessionRepository extends JpaRepository<LearningSession, Long> {

    // Custom method to get sessions where the current user is either learner or teacher
    List<LearningSession> findByLearnerOrTeacher(User learner, User teacher);
}
