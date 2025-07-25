package com.skillexchange.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String bio;
    private String skillsOffered;
    private String skillsNeeded;
}
