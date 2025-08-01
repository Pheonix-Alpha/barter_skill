package com.skillexchange.controller;

import com.skillexchange.dto.AdminRequestDTO;
import com.skillexchange.dto.AdminUserDTO;
import com.skillexchange.dto.LessonResponseDto;
import com.skillexchange.service.AdminService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/dashboard/stats")
    public Map<String, Object> getAdminStats() {
        return adminService.getAdminStats();
    }

    @GetMapping("/lessons")
    public ResponseEntity<List<LessonResponseDto>> getAllLessons() {
        return ResponseEntity.ok(adminService.getAllLessonDtos());
    }

    @DeleteMapping("/lessons/{id}")
    public ResponseEntity<Map<String, String>> deleteLesson(@PathVariable Long id) {
        return adminService.deleteLessonById(id)
                ? ResponseEntity.ok(Map.of("message", "Lesson deleted"))
                : ResponseEntity.status(404).body(Map.of("message", "Lesson not found"));
    }

    @GetMapping("/users")
    public ResponseEntity<List<AdminUserDTO>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllAdminUserDtos());
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
        return adminService.deleteUserById(id)
                ? ResponseEntity.ok(Map.of("message", "User deleted"))
                : ResponseEntity.status(404).body(Map.of("message", "User not found"));
    }

     @GetMapping("/requests")
    public ResponseEntity<List<AdminRequestDTO>> getAllRequests() {
        return ResponseEntity.ok(adminService.getAllRequestsForAdmin());
    }

    // 🔥 NEW: Delete exchange request by ID
    @DeleteMapping("/requests/{id}")
    public ResponseEntity<Map<String, String>> deleteRequest(@PathVariable Long id) {
        return adminService.deleteRequestById(id)
                ? ResponseEntity.ok(Map.of("message", "Request deleted"))
                : ResponseEntity.status(404).body(Map.of("message", "Request not found"));
    }

    //
}
