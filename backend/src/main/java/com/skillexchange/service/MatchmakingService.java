package com.skillexchange.service;

import com.skillexchange.model.SkillType;
import com.skillexchange.model.User;
import com.skillexchange.model.UserSkill;
import com.skillexchange.repository.UserRepository;
import com.skillexchange.repository.UserSkillRepository;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MatchmakingService {

    private final UserSkillRepository userSkillRepo;
    private final UserRepository userRepo;

    public MatchmakingService(UserSkillRepository userSkillRepo, UserRepository userRepo) {
        this.userSkillRepo = userSkillRepo;
        this.userRepo = userRepo;
    }

    /**
     * Get the currently authenticated user from the security context.
     */
    @Transactional
    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepo.findWithRelationsByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    /**
     * Return users who are offering a specific skill (by ID).
     */
    @Transactional
    public List<User> findUsersOfferingSkill(Long skillId) {
        return userSkillRepo.findBySkillIdAndType(skillId, SkillType.OFFERED).stream()
                .map(UserSkill::getUser)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Return users who want a specific skill (by ID).
     */
   @Transactional
public List<User> findUsersWantingSkill(Long skillId) {
    List<User> users = userSkillRepo.findBySkillIdAndType(skillId, SkillType.WANTED).stream()
            .map(UserSkill::getUser)
            .distinct()
            .toList();


    return users;
}


    /**
     * Find mutual matches: users who offer what I want and want what I offer.
     */
    public List<User> findSkillMatches() {
        User me = getCurrentUser();

        Set<Long> myOfferedSkillIds = userSkillRepo.findByUserIdAndType(me.getId(), SkillType.OFFERED).stream()
                .map(us -> us.getSkill().getId())
                .collect(Collectors.toSet());

        Set<Long> myWantedSkillIds = userSkillRepo.findByUserIdAndType(me.getId(), SkillType.WANTED).stream()
                .map(us -> us.getSkill().getId())
                .collect(Collectors.toSet());

        Set<User> matches = new HashSet<>();

        for (Long wantedId : myWantedSkillIds) {
            List<User> usersOffering = userSkillRepo.findBySkillIdAndType(wantedId, SkillType.OFFERED).stream()
                    .map(UserSkill::getUser)
                    .distinct()
                    .toList();

            for (User candidate : usersOffering) {
                Set<Long> theirWantedSkills = userSkillRepo.findByUserIdAndType(candidate.getId(), SkillType.WANTED).stream()
                        .map(us -> us.getSkill().getId())
                        .collect(Collectors.toSet());

                if (!Collections.disjoint(theirWantedSkills, myOfferedSkillIds)) {
                    matches.add(candidate);
                }
            }
        }

        return new ArrayList<>(matches);
    }

    /**
     * Search users offering a skill by skill name.
     */
    @Transactional
    public List<User> searchUsersOfferingSkill(String skillName) {
        return userRepo.findBySkillNameAndType(skillName, SkillType.OFFERED);
    }

    /**
     * Search users wanting a skill by skill name.
     */
    
   @Transactional
public List<User> searchUsersWantingSkill(String skillName) {
    List<User> users = userRepo.findBySkillNameAndType(skillName, SkillType.WANTED);
    users.forEach(user -> user.getUserSkills().size()); // Force initialization
    return users;
}


    /**
     * Combined search — return users either offering or wanting the given skill.
     */
    @Transactional
    public List<User> searchUsersBySkill(String skillName) {
        Set<User> combined = new HashSet<>();
        combined.addAll(searchUsersOfferingSkill(skillName));
        combined.addAll(searchUsersWantingSkill(skillName));
        return new ArrayList<>(combined);
    }
}
