package com.skillexchange.controller;

import com.skillexchange.dto.UserDTO;
import com.skillexchange.model.User;
import com.skillexchange.service.MatchmakingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/api/match")
public class MatchmakingController {

    private final MatchmakingService matchmakingService;

    public MatchmakingController(MatchmakingService matchmakingService) {
        this.matchmakingService = matchmakingService;
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
    public List<UserDTO> getMyMatches() {
        User currentUser = matchmakingService.getCurrentUser();
        return matchmakingService.findSkillMatches()
                .stream()
                .map(user -> new UserDTO(user, currentUser))
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
