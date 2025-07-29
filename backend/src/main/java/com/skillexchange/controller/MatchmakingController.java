package com.skillexchange.controller;

import com.skillexchange.dto.UserDTO;
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
public List<UserDTO> usersOfferingSkill(@RequestParam String skill) {
    User currentUser = matchmakingService.getCurrentUser();
    return matchmakingService.searchUsersOfferingSkill(skill)
            .stream()
            .map(user -> new UserDTO(user, currentUser))
            .toList();
}

@GetMapping("/wanting")
public List<UserDTO> usersWantingSkill(@RequestParam String skill) {
    User currentUser = matchmakingService.getCurrentUser();
    return matchmakingService.searchUsersWantingSkill(skill)
            .stream()
            .map(user -> new UserDTO(user, currentUser))
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
