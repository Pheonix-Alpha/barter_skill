package com.skillexchange.service;

import com.skillexchange.dto.ProfileUpdateRequest;
import com.skillexchange.model.*;
import com.skillexchange.repository.SkillRepository;
import com.skillexchange.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserProfileService {

    private final UserRepository userRepo;
    private final SkillRepository skillRepo;

    public UserProfileService(UserRepository userRepo, SkillRepository skillRepo) {
        this.userRepo = userRepo;
        this.skillRepo = skillRepo;
    }

    @Transactional
    public void updateUserProfile(User user, ProfileUpdateRequest request) {
        if (request.getUsername() != null && !request.getUsername().isBlank()) {
            user.setUsername(request.getUsername());
        }

        if (request.getBio() != null) {
            user.setBio(request.getBio());
        }

        if (request.getProfilePicture() != null) {
            user.setProfilePicture(request.getProfilePicture());
        }

        // Replace OFFERED skills if provided
        if (request.getOfferingSkills() != null) {
            user.getUserSkills().removeIf(us -> us.getType() == SkillType.OFFERED);
            addSkillsToUser(user, request.getOfferingSkills(), SkillType.OFFERED);
        }

        // Replace WANTED skills if provided
        if (request.getWantingSkills() != null) {
            user.getUserSkills().removeIf(us -> us.getType() == SkillType.WANTED);
            addSkillsToUser(user, request.getWantingSkills(), SkillType.WANTED);
        }

        userRepo.save(user);
    }

    private void addSkillsToUser(User user, List<String> skillNames, SkillType type) {
        for (String originalSkillName : skillNames) {
            final String skillName = originalSkillName.trim();

            Skill skill = skillRepo.findByNameIgnoreCase(skillName)
                    .orElseGet(() -> {
                        Skill newSkill = new Skill();
                        newSkill.setName(skillName);
                        return skillRepo.save(newSkill);
                    });

            UserSkill userSkill = new UserSkill();
            userSkill.setSkill(skill);
            userSkill.setUser(user);
            userSkill.setType(type);

            user.getUserSkills().add(userSkill);
        }
    }
}
