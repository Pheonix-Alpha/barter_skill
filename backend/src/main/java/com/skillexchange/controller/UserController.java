package com.skillexchange.controller;

import com.skillexchange.dto.UserDTO;
import com.skillexchange.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDTO> getAllUsersExceptMe(Authentication auth) {
        return userService.getAllUsersExcept(auth.getName());
    }
}
