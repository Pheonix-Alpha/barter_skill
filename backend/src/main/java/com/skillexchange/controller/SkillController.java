package com.skillexchange.controller;

import com.skillexchange.model.Skill;
import com.skillexchange.service.SkillService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skills")
public class SkillController {

    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping("/add")
    public Skill addSkill(@RequestParam String name) {
        return skillService.addSkill(name);
    }

    @GetMapping
    public List<Skill> getAllSkills() {
        return skillService.getAllSkills();
    }
}
