package com.skillexchange.service;
import org.springframework.transaction.annotation.Transactional;

import com.skillexchange.dto.SkillRequestDto;
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

     @Transactional(readOnly = true)
   public User getCurrentUser() {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    return userRepo.findWithRelationsByUsername(username)
                   .orElseThrow(() -> new RuntimeException("User not found"));
}


   public UserSkill offerOrRequestSkill(SkillRequestDto req) {
    Skill skill = skillRepo.findById(req.getSkillId())
            .orElseThrow(() -> new RuntimeException("Skill not found"));
    User user = getCurrentUser();

    SkillType type;
    try {
        type = SkillType.valueOf(req.getType().toUpperCase());
    } catch (IllegalArgumentException | NullPointerException e) {
        throw new IllegalArgumentException("Invalid or missing skill type: " + req.getType());
    }

    UserSkill us = UserSkill.builder()
            .user(user)
            .skill(skill)
            .type(type)
            .build();

    return userSkillRepo.save(us);
}


    public List<UserSkill> getMySkills() {
        return userSkillRepo.findByUser(getCurrentUser());
    }
}
