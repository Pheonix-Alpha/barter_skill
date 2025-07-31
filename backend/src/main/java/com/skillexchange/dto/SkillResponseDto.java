package com.skillexchange.dto;

import java.time.LocalDateTime;

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

      private LocalDateTime createdAt;
    private LocalDateTime updatedAt;



    public SkillResponseDto(
    Long id,
    String type,
    RequestStatus status,
    UserDTO requester,
    UserDTO target,
    String offeredSkillName,
    String wantedSkillName,
    Long currentUserId
) {
    this.id = id;
    this.type = type;
    this.status = status;
    this.requester = requester;
    this.target = target;
    this.offeredSkillName = offeredSkillName;
    this.wantedSkillName = wantedSkillName;
    this.currentUserId = currentUserId;
}

     
}

