package com.skillexchange.dto;

import com.skillexchange.model.User;
import lombok.Data;

@Data
public class AdminUserDTO {
    private Long id;
    private String username;
    private String email;
    private String bio;
    private String profilePicture;
    private String role;

   public static AdminUserDTO fromEntity(User user) {
    AdminUserDTO dto = new AdminUserDTO();
    dto.setId(user.getId());
    dto.setUsername(user.getUsername());
    dto.setEmail(user.getEmail());
    dto.setBio(user.getBio());
    dto.setProfilePicture(user.getProfilePicture());

    // Null-safe role assignment
    dto.setRole(user.getRole() != null ? user.getRole().name() : "UNKNOWN");

    return dto;
}

}
