package com.skillexchange.dto;

import com.skillexchange.model.SkillExchangeRequest;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminRequestDTO {
    private Long id;
    private Long requesterId;
    private String requesterUsername;
    private Long targetId;
    private String targetUsername;
    private String wantedSkill;
    private String offeredSkill;
    private String status;
    private String type;
    private LocalDateTime createdAt;

   
    public static AdminRequestDTO fromEntity(SkillExchangeRequest req) {
    AdminRequestDTO dto = new AdminRequestDTO();
    dto.setId(req.getId());
    dto.setRequesterId(req.getRequester().getId());
    dto.setRequesterUsername(req.getRequester().getUsername());
    dto.setTargetId(req.getTarget().getId());
    dto.setTargetUsername(req.getTarget().getUsername());
    dto.setWantedSkill(req.getWantedSkill() != null ? req.getWantedSkill().getName() : null);
    dto.setOfferedSkill(req.getOfferedSkill() != null ? req.getOfferedSkill().getName() : null);
    dto.setStatus(req.getStatus() != null ? req.getStatus().name() : "UNKNOWN");
    dto.setType(req.getType() != null ? req.getType().name() : "UNKNOWN");
    dto.setCreatedAt(req.getCreatedAt());
    return dto;
}


}
