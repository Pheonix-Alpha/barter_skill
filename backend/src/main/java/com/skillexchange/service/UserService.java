package com.skillexchange.service;

import com.skillexchange.dto.UserDTO;
import com.skillexchange.model.User;
import com.skillexchange.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepo;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsersExcept(String currentUsername) {
        User currentUser = userRepo.findWithRelationsByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return userRepo.findAll().stream()
                .filter(u -> !u.getUsername().equals(currentUsername))
                .map(u -> new UserDTO(u, currentUser))
                .toList();
    }
}
