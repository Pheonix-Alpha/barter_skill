package com.skillexchange.dto;

import com.skillexchange.model.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkillResponseDto {
    private Long id;
    private String type;
    private RequestStatus status;

    private UserDTO requester;
    private UserDTO target;

    private String offeredSkillName;
    private String wantedSkillName;

     private Long currentUserId;
}
