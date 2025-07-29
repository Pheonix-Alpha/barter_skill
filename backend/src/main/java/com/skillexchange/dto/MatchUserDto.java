package com.skillexchange.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchUserDto {
    private Long id;
    private String username;
    private String bio;
    private List<String> offeringSkills;
    private List<String> wantingSkills;
    private List<Long> offeringSkillIds;
    private List<Long> wantingSkillIds;
}
