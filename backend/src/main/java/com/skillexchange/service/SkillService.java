package com.skillexchange.service;

import com.skillexchange.model.Skill;
import com.skillexchange.repository.SkillRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillService {

    private final SkillRepository skillRepo;

    public SkillService(SkillRepository skillRepo) {
        this.skillRepo = skillRepo;
    }

    public Skill addSkill(String name) {
        Skill skill = Skill.builder().name(name).build();
        return skillRepo.save(skill);
    }

    public List<Skill> getAllSkills() {
        return skillRepo.findAll();
    }
}
