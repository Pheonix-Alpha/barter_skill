package com.skillexchange.service;

import com.skillexchange.model.*;
import com.skillexchange.repository.UserRepository;
import com.skillexchange.repository.UserSkillRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepo.findByUsername(username).orElseThrow();
    }

    // Find users offering a skill
    public List<User> findUsersOfferingSkill(Long skillId) {
        return userSkillRepo.findBySkillIdAndType(skillId, SkillType.OFFERED).stream()
                .map(UserSkill::getUser)
                .distinct()
                .collect(Collectors.toList());
    }

    // Find users wanting a skill
    public List<User> findUsersWantingSkill(Long skillId) {
        return userSkillRepo.findBySkillIdAndType(skillId, SkillType.WANTED).stream()
                .map(UserSkill::getUser)
                .distinct()
                .collect(Collectors.toList());
    }

    // Smart matchmaking: find users who want what I offer and offer what I want
    public List<User> findSkillMatches() {
        User me = getCurrentUser();

        List<Long> myOfferedSkillIds = userSkillRepo.findByUserIdAndType(me.getId(), SkillType.OFFERED).stream()
                .map(us -> us.getSkill().getId()).toList();

        List<Long> myWantedSkillIds = userSkillRepo.findByUserIdAndType(me.getId(), SkillType.WANTED).stream()
                .map(us -> us.getSkill().getId()).toList();

        Set<User> matches = new HashSet<>();

        for (Long skillIWant : myWantedSkillIds) {
            List<User> usersOfferingIt = userSkillRepo.findBySkillIdAndType(skillIWant, SkillType.OFFERED).stream()
                    .map(UserSkill::getUser).toList();

            for (User user : usersOfferingIt) {
                List<Long> theirWantedSkills = userSkillRepo.findByUserIdAndType(user.getId(), SkillType.WANTED).stream()
                        .map(us -> us.getSkill().getId()).toList();

                if (!Collections.disjoint(theirWantedSkills, myOfferedSkillIds)) {
                    matches.add(user);
                }
            }
        }

        return new ArrayList<>(matches);
    }
}
