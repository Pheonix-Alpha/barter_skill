package com.skillexchange.service;

import com.skillexchange.model.SkillType;
import com.skillexchange.model.User;
import com.skillexchange.model.UserSkill;
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

    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<User> findUsersOfferingSkill(Long skillId) {
        return userSkillRepo.findBySkillIdAndType(skillId, SkillType.OFFERED).stream()
                .map(UserSkill::getUser)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<User> findUsersWantingSkill(Long skillId) {
        return userSkillRepo.findBySkillIdAndType(skillId, SkillType.WANTED).stream()
                .map(UserSkill::getUser)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<User> findSkillMatches() {
        User me = getCurrentUser();

        List<Long> myOfferedSkillIds = userSkillRepo.findByUserIdAndType(me.getId(), SkillType.OFFERED).stream()
                .map(us -> us.getSkill().getId())
                .toList();

        List<Long> myWantedSkillIds = userSkillRepo.findByUserIdAndType(me.getId(), SkillType.WANTED).stream()
                .map(us -> us.getSkill().getId())
                .toList();

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

    // New: Find users offering a skill by skill name
    public List<User> searchUsersOfferingSkill(String skillName) {
        return userRepo.findBySkillNameAndType(skillName, SkillType.OFFERED);
    }

    // New: Find users wanting a skill by skill name
    public List<User> searchUsersWantingSkill(String skillName) {
        return userRepo.findBySkillNameAndType(skillName, SkillType.WANTED);
    }
    public List<User> searchUsersBySkill(String skillName) {
    Set<User> combined = new HashSet<>();
    combined.addAll(searchUsersOfferingSkill(skillName));
    combined.addAll(searchUsersWantingSkill(skillName));
    return new ArrayList<>(combined);
}

}
