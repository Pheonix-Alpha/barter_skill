package com.skillexchange.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkillResponseDto {
    private Long id;
    private String requesterUsername;
    private String targetUsername;
    private Long targetUserId;  
    private String skillName;
    private String status;
}
