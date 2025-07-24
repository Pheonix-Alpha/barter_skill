package com.skillexchange.controller;

import com.skillexchange.dto.SkillRequestDto;
import com.skillexchange.model.UserSkill;
import com.skillexchange.service.UserSkillService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-skills")
public class UserSkillController {

    private final UserSkillService userSkillService;

    public UserSkillController(UserSkillService userSkillService) {
        this.userSkillService = userSkillService;
    }

    @PostMapping("/add")
    public UserSkill addUserSkill(@RequestBody SkillRequestDto request) {
        return userSkillService.offerOrRequestSkill(request);
    }

    @GetMapping("/my")
    public List<UserSkill> getMySkills() {
        return userSkillService.getMySkills();
    }
}
