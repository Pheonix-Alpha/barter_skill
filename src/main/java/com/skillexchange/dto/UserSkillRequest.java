package com.skillexchange.dto;

import com.skillexchange.model.SkillType;
import lombok.Data;

@Data
public class UserSkillRequest {
    private Long skillId;
    private SkillType type; // OFFERED or WANTED
}
