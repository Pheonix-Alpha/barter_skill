package com.skillexchange.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillexchange.model.User;
import com.skillexchange.repository.UserRepository;

@RestController
@RequestMapping("/api/test")
public class TestController {

     @GetMapping("/hello")
    public String hello() {
        return "🔓 Public: No token needed";
    }

    @GetMapping("/secure-hello")
    public String secureHello() {
        return "🔐 Protected: You are authenticated!";
    }
}
