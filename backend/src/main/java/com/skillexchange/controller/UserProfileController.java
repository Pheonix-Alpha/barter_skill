package com.skillexchange.controller;

import com.skillexchange.model.User;
import com.skillexchange.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserProfileController {

    private final UserRepository userRepo;

    public UserProfileController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable Long userId) {
        return userRepo.findById(userId).orElseThrow();
    }
}
