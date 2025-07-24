package com.skillexchange.controller;

import com.skillexchange.dto.SkillRequestDto;
import com.skillexchange.dto.SkillResponseDto;
import com.skillexchange.model.RequestStatus;
import com.skillexchange.service.SkillExchangeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exchange")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class SkillExchangeController {

    private final SkillExchangeService exchangeService;

    public SkillExchangeController(SkillExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    /**
     * Create a new skill exchange request from current user to target user.
     */
    @PostMapping("/request")
    public ResponseEntity<SkillResponseDto> requestSkill(@RequestBody SkillRequestDto dto) {
        SkillResponseDto createdRequest = exchangeService.createRequest(dto);
        return ResponseEntity.ok(createdRequest);
    }

    /**
     * Retrieve all skill exchange requests sent or received by current user.
     */
    @GetMapping("/my-requests")
    public ResponseEntity<List<SkillResponseDto>> getRequests() {
        List<SkillResponseDto> requests = exchangeService.getMyRequests();
        return ResponseEntity.ok(requests);
    }

    /**
     * Respond to an existing skill exchange request by updating its status.
     */
    @PutMapping("/respond/{id}")
    public ResponseEntity<SkillResponseDto> respond(
            @PathVariable Long id,
            @RequestParam RequestStatus status) {
        SkillResponseDto updatedRequest = exchangeService.respondToRequest(id, status);
        return ResponseEntity.ok(updatedRequest);
    }
}
