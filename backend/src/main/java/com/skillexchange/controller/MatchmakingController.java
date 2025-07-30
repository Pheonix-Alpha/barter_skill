package com.skillexchange.controller;

import com.skillexchange.dto.UserDTO;
import com.skillexchange.model.SkillType;
import com.skillexchange.model.User;
import com.skillexchange.service.MatchmakingService;
import org.springframework.web.bind.annotation.*;
import com.skillexchange.repository.UserRepository;


import java.util.List;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/api/match")
public class MatchmakingController {

    private final MatchmakingService matchmakingService;
    private final UserRepository userRepo;

    public MatchmakingController(MatchmakingService matchmakingService, UserRepository userRepo) {
        this.matchmakingService = matchmakingService;
        this.userRepo = userRepo;
    }

  @GetMapping("/offering")
public List<UserDTO> searchByOffering(@RequestParam String skill) {
    User currentUser = matchmakingService.getCurrentUser();

    // Step 1: Search users who offer the skill
    List<User> raw = userRepo.findBySkillNameAndType(skill, SkillType.OFFERED);

    // Step 2: Fetch all their skills
    List<User> usersWithSkills = userRepo.findAllWithSkills(raw);

    // Step 3: Build DTOs with full data
    return usersWithSkills.stream()
            .map(u -> new UserDTO(u, currentUser))
            .toList();
}

@GetMapping("/wanting")
public List<UserDTO> searchByWanting(@RequestParam String skill) {
    User currentUser = matchmakingService.getCurrentUser();

    List<User> raw = userRepo.findBySkillNameAndType(skill, SkillType.WANTED);
    List<User> usersWithSkills = userRepo.findAllWithSkills(raw);

    return usersWithSkills.stream()
            .map(u -> new UserDTO(u, currentUser))
            .toList();
}



 @GetMapping("/matches")
public List<UserDTO> getSkillMatches() {
    User currentUser = matchmakingService.getCurrentUser();
    List<User> rawMatches = matchmakingService.findSkillMatches();

    // Fetch skills eagerly to avoid LazyInitializationException
    List<User> matchesWithSkills = userRepo.findAllWithSkills(rawMatches);

    return matchesWithSkills.stream()
        .map(u -> new UserDTO(u, currentUser))
        .toList();
}


    @GetMapping("/search")
    public List<UserDTO> searchUsersBySkill(@RequestParam String skill) {
        User currentUser = matchmakingService.getCurrentUser();
        return matchmakingService.searchUsersBySkill(skill)
                .stream()
                .map(user -> new UserDTO(user, currentUser))
                .toList();
    }
}
