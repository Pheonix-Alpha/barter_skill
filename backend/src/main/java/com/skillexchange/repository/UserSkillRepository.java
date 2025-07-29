package com.skillexchange.repository;

import com.skillexchange.model.Skill;
import com.skillexchange.model.SkillType;
import com.skillexchange.model.User;
import com.skillexchange.model.UserSkill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserSkillRepository extends JpaRepository<UserSkill, Long> {
    List<UserSkill> findByUser(User user);
    List<UserSkill> findBySkillIdAndType(Long skillId, SkillType type);

List<UserSkill> findByUserIdAndType(Long userId, SkillType type);

boolean existsByUserAndSkillAndType(User user, Skill skill, SkillType type);


}
