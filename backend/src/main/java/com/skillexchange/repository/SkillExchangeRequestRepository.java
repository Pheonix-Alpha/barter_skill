package com.skillexchange.repository;

import com.skillexchange.model.SkillExchangeRequest;
import com.skillexchange.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SkillExchangeRequestRepository extends JpaRepository<SkillExchangeRequest, Long> {
    List<SkillExchangeRequest> findByRequester(User user);
    List<SkillExchangeRequest> findByTarget(User user);
}
