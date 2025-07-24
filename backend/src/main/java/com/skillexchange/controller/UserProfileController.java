package com.skillexchange.controller;

import com.skillexchange.dto.UserDTO;
import com.skillexchange.model.User;
import com.skillexchange.repository.UserRepository;
import com.skillexchange.service.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/users")
public class UserProfileController {

    private final UserRepository userRepo;

    public UserProfileController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    // ✅ Get profile of any user with relationship flags
    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserProfile(
            @PathVariable Long userId,
            @AuthenticationPrincipal CustomUserDetails currentUserDetails
    ) {
        User targetUser = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        User currentUser = currentUserDetails.getUser();

        UserDTO dto = new UserDTO(targetUser, currentUser);
        return ResponseEntity.ok(dto);
    }

    // ✅ Get profile of the currently authenticated user (now safe and unique path)
   @GetMapping("/me/profile")
public ResponseEntity<UserDTO> getCurrentUserProfile(Authentication authentication) {
    String username = authentication.getName();
    User currentUser = userRepo.findWithRelationsByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    return ResponseEntity.ok(new UserDTO(currentUser, currentUser));
}

}
