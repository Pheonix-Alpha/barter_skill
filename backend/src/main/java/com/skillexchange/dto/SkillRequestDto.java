package com.skillexchange.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkillRequestDto {

    @NotNull(message = "wantedSkillId is required.")
    private Long wantedSkillId;

    @NotNull(message = "offeredSkillId is required.")
    private Long offeredSkillId;

    @NotNull(message = "targetUserId is required.")
    private Long targetUserId;

    @NotNull(message = "type is required.")
    @Pattern(regexp = "^(OFFERED|WANTED)$", message = "type must be either 'OFFERED' or 'WANTED'.")
    private String type;
}
