package com.skillexchange.controller;

import com.skillexchange.model.Skill;
import com.skillexchange.model.SkillExchangeRequest;
import com.skillexchange.model.User;
import com.skillexchange.repository.SkillExchangeRequestRepository;
import com.skillexchange.repository.SkillRepository;
import com.skillexchange.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserRepository userRepo;
    private final SkillRepository skillRepo;
    private final SkillExchangeRequestRepository exchangeRepo;

    public AdminController(UserRepository userRepo, SkillRepository skillRepo, SkillExchangeRequestRepository exchangeRepo) {
        this.userRepo = userRepo;
        this.skillRepo = skillRepo;
        this.exchangeRepo = exchangeRepo;
    }

    @GetMapping("/users")
    public List<User> allUsers() {
        return userRepo.findAll();
    }

    @GetMapping("/skills")
    public List<Skill> allSkills() {
        return skillRepo.findAll();
    }

    @GetMapping("/exchanges")
    public List<SkillExchangeRequest> allExchanges() {
        return exchangeRepo.findAll();
    }

    @PutMapping("/block/{id}")
    public String blockUser(@PathVariable Long id) {
        User user = userRepo.findById(id).orElseThrow();
        user.setActive(false);  // ✅ Correct setter

        userRepo.save(user);
        return "User blocked";
    }

    @PutMapping("/unblock/{id}")
    public String unblockUser(@PathVariable Long id) {
        User user = userRepo.findById(id).orElseThrow();
        user.setActive(true);
        userRepo.save(user);
        return "User unblocked";
    }

    @DeleteMapping("/delete-user/{id}")
    public String deleteUser(@PathVariable Long id) {
        userRepo.deleteById(id);
        return "User deleted";
    }

    @PutMapping("/promote/{id}")
    public String makeAdmin(@PathVariable Long id) {
        User user = userRepo.findById(id).orElseThrow(() -> new RuntimeException("User not found with id " + id))
;
        user.setRole(com.skillexchange.model.Role.ADMIN);
        userRepo.save(user);
        return "User promoted to ADMIN";
    }
}
