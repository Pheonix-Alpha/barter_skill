package com.skillexchange.controller;

import com.skillexchange.model.User;
import com.skillexchange.service.MatchmakingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/match")
public class MatchmakingController {

    private final MatchmakingService matchmakingService;

    public MatchmakingController(MatchmakingService matchmakingService) {
        this.matchmakingService = matchmakingService;
    }

    @GetMapping("/offering/{skillId}")
    public List<User> usersOffering(@PathVariable Long skillId) {
        return matchmakingService.findUsersOfferingSkill(skillId);
    }

    @GetMapping("/wanting/{skillId}")
    public List<User> usersWanting(@PathVariable Long skillId) {
        return matchmakingService.findUsersWantingSkill(skillId);
    }

    @GetMapping("/matches")
    public List<User> myMatches() {
        return matchmakingService.findSkillMatches();
    }
}
