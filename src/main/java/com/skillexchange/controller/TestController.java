package com.skillexchange.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillexchange.model.User;
import com.skillexchange.repository.UserRepository;

@RestController
@RequestMapping("/api/test")
public class TestController {

    private final UserRepository userRepo;

    public TestController(UserRepository userRepo){
        this.userRepo = userRepo;

    }
    @PostMapping("/add")
    public User addUser(@RequestBody User user){
        return userRepo.save(user);
    }
}
