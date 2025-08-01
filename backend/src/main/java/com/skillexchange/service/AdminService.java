package com.skillexchange.service;
import com.skillexchange.dto.AdminRequestDTO;
import com.skillexchange.dto.AdminUserDTO;
import com.skillexchange.dto.LessonResponseDto;
import com.skillexchange.model.Lesson;


import com.skillexchange.repository.*;
import com.skillexchange.controller.LessonMapper;


import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final UserRepository userRepo;
    private final SkillExchangeRequestRepository exchangeRepo;
    private final LessonRepository lessonRepo;
    private final ChatMessageRepository chatRepo;
    


    public AdminService(UserRepository userRepo,
                        SkillExchangeRequestRepository exchangeRepo,
                        LessonRepository lessonRepo,
                        ChatMessageRepository chatRepo) {
        this.userRepo = userRepo;
        this.exchangeRepo = exchangeRepo;
        this.lessonRepo = lessonRepo;
        this.chatRepo = chatRepo;
    }

    public Map<String, Object> getAdminStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userRepo.count());
        stats.put("totalExchanges", exchangeRepo.count());
        stats.put("lessonsScheduled", lessonRepo.count());
        stats.put("requestCount", exchangeRepo.count());
        stats.put("activeChats", chatRepo.count()); 

        return stats;
    }

    public List<LessonResponseDto> getAllLessonDtos() {
        List<Lesson> lessons = lessonRepo.findAll();
        return lessons.stream()
                .map(LessonMapper::toDto)
                .collect(Collectors.toList());
    }

    public boolean deleteLessonById(Long id) {
        if (!lessonRepo.existsById(id)) return false;
        lessonRepo.deleteById(id);
        return true;
    }

    public List<AdminUserDTO> getAllAdminUserDtos() {
        return userRepo.findAll().stream()
                .map(AdminUserDTO::fromEntity)
                .collect(Collectors.toList());
    }

 @Transactional
public boolean deleteUserById(Long userId) {
    if (!userRepo.existsById(userId)) {
        return false;
    }

    chatRepo.deleteBySenderId(userId);
    chatRepo.deleteByReceiverId(userId);
    userRepo.deleteById(userId);
    return true;
}

@Autowired
private SkillExchangeRequestRepository skillExchangeRequestRepository;

public List<AdminRequestDTO> getAllRequestsForAdmin() {
    return skillExchangeRequestRepository.findAllWithDetails()  // ✅ USE THIS
            .stream()
            .map(AdminRequestDTO::fromEntity)
            .toList();
}


@Transactional
public boolean deleteRequestById(Long requestId) {
    if (!exchangeRepo.existsById(requestId)) {
        return false;
    }
    exchangeRepo.deleteById(requestId);
    return true;
}



}
