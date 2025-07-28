package com.skillexchange.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AcceptedRequestDto {
    private Long userId;
    private String username;
    private Long skillId;
    private String skillName;
}
