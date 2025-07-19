package com.skillexchange.dto;

import lombok.Data;

@Data
public class SkillExchangeRequestDto {
    private Long skillId;
    private Long targetUserId;
}
