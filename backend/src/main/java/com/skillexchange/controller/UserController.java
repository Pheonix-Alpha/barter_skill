// File: UserController.java
package com.skillexchange.controller;

import com.skillexchange.model.User;
import com.skillexchange.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepo;

    public UserController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping("/all")
    public List<User> getAllUsersExceptMe(Authentication auth) {
        String currentUsername = auth.getName();
        return userRepo.findAll()
                .stream()
                .filter(user -> !user.getUsername().equals(currentUsername))
                .toList();
    }
}
