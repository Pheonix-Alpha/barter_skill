package com.skillexchange.service;

import com.skillexchange.dto.UserSkillRequest;
import com.skillexchange.model.*;
import com.skillexchange.repository.SkillRepository;
import com.skillexchange.repository.UserRepository;
import com.skillexchange.repository.UserSkillRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserSkillService {

    private final UserSkillRepository userSkillRepo;
    private final SkillRepository skillRepo;
    private final UserRepository userRepo;

    public UserSkillService(UserSkillRepository userSkillRepo, SkillRepository skillRepo, UserRepository userRepo) {
        this.userSkillRepo = userSkillRepo;
        this.skillRepo = skillRepo;
        this.userRepo = userRepo;
    }

    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepo.findByUsername(username).orElseThrow();
    }

    public UserSkill offerOrRequestSkill(UserSkillRequest req) {
        Skill skill = skillRepo.findById(req.getSkillId()).orElseThrow();
        User user = getCurrentUser();

        UserSkill us = UserSkill.builder()
                .user(user)
                .skill(skill)
                .type(req.getType())
                .build();

        return userSkillRepo.save(us);
    }

    public List<UserSkill> getMySkills() {
        return userSkillRepo.findByUser(getCurrentUser());
    }
}
