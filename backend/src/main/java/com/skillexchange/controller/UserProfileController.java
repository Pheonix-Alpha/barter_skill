package com.skillexchange.controller;

import com.skillexchange.dto.ProfileUpdateRequest;
import com.skillexchange.dto.UserDTO;
import com.skillexchange.model.User;
import com.skillexchange.repository.UserRepository;
import com.skillexchange.service.CustomUserDetails;
import com.skillexchange.service.UserProfileService;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserProfileController {

    private final UserRepository userRepo;
    private final UserProfileService profileService;

    public UserProfileController(UserRepository userRepo, UserProfileService profileService) {
        this.userRepo = userRepo;
        this.profileService = profileService;
    }

    // ✅ Get profile of any user with relationship flags
    @GetMapping("/{userId}")
    @Transactional
    public ResponseEntity<UserDTO> getUserProfile(
            @PathVariable Long userId,
            @AuthenticationPrincipal CustomUserDetails currentUserDetails
    ) {
        User targetUser = userRepo.findWithRelationsById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        User currentUser = userRepo.findWithRelationsById(currentUserDetails.getUser().getId())
                .orElseThrow(() -> new RuntimeException("Current user not found"));

        return ResponseEntity.ok(new UserDTO(targetUser, currentUser));
    }

    // ✅ Get profile of the currently authenticated user
    @GetMapping("/me/profile")
    @Transactional
    public ResponseEntity<UserDTO> getCurrentUserProfile(Authentication authentication) {
        String username = authentication.getName();
        User currentUser = userRepo.findWithRelationsByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return ResponseEntity.ok(new UserDTO(currentUser, currentUser));
    }

    // ✅ Update profile of currently authenticated user
    @PutMapping("/update")
    public ResponseEntity<String> updateProfile(
            @RequestBody ProfileUpdateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        User user = userRepo.findWithRelationsById(userDetails.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        profileService.updateUserProfile(user, request);
        return ResponseEntity.ok("Profile updated successfully");
    }
}
