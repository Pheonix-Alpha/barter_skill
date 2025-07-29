package com.skillexchange.service;

import com.skillexchange.dto.SkillRequestDto;
import com.skillexchange.exception.ResourceNotFoundException;
import com.skillexchange.model.*;
import com.skillexchange.repository.SkillRepository;
import com.skillexchange.repository.UserRepository;
import com.skillexchange.repository.UserSkillRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserSkillService {

    private final UserSkillRepository userSkillRepo;
    private final SkillRepository skillRepo;
    private final UserRepository userRepo;

    public UserSkillService(UserSkillRepository userSkillRepo,
                            SkillRepository skillRepo,
                            UserRepository userRepo) {
        this.userSkillRepo = userSkillRepo;
        this.skillRepo = skillRepo;
        this.userRepo = userRepo;
    }

    @Transactional(readOnly = true)
    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepo.findWithRelationsByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }

    /**
     * Save a skill for the current user, either as OFFERED or WANTED.
     */
 @Transactional
public UserSkill offerOrRequestSkill(SkillRequestDto req) {
    if (req.getType() == null) {
        throw new IllegalArgumentException("Skill type is required.");
    }

    SkillType type;
    try {
        type = SkillType.valueOf(req.getType().toUpperCase());
    } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException("Invalid skill type: " + req.getType());
    }

    Long skillId = (type == SkillType.OFFERED) ? req.getOfferedSkillId() : req.getWantedSkillId();
    if (skillId == null) {
        throw new IllegalArgumentException("Skill ID is required for type: " + type);
    }

    Skill skill = skillRepo.findById(skillId)
            .orElseThrow(() -> new RuntimeException("Skill not found"));

    User user = getCurrentUser();

    // ✅ Duplicate check
    if (userSkillRepo.existsByUserAndSkillAndType(user, skill, type)) {
        throw new IllegalStateException("Skill already marked as " + type + " by the user.");
    }

    UserSkill us = UserSkill.builder()
            .user(user)
            .skill(skill)
            .type(type)
            .build();

    return userSkillRepo.save(us);
}

    /**
     * Return all skills (offered or wanted) of current user.
     */
    @Transactional(readOnly = true)
    public List<UserSkill> getMySkills() {
        return userSkillRepo.findByUser(getCurrentUser());
    }
}
