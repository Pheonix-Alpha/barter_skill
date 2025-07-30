package com.skillexchange.dto;

import com.skillexchange.model.SkillType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SentRequestDto {
    private Long targetId;
    private Long skillId;
    private SkillType type;
}
