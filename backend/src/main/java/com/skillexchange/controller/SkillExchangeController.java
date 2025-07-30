package com.skillexchange.controller;

import com.skillexchange.dto.AcceptedRequestDto;
import com.skillexchange.dto.SentRequestDto;
import com.skillexchange.dto.SkillRequestDto;
import com.skillexchange.dto.SkillResponseDto;
import com.skillexchange.model.RequestStatus;
import com.skillexchange.service.SkillExchangeService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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
public ResponseEntity<SkillResponseDto> requestSkill(@Valid @RequestBody SkillRequestDto dto) {
    System.out.println("📥 Incoming skill request: " + dto);
    SkillResponseDto createdRequest = exchangeService.createRequest(dto);
    return ResponseEntity.ok(createdRequest);
}

    /**
     * Retrieve all skill exchange requests sent or received by current user.
     */
    @GetMapping("/my-requests")
    public ResponseEntity<List<SkillResponseDto>> getRequests() {
         System.out.println("🔍 Authenticated principal: " + SecurityContextHolder.getContext().getAuthentication());
    System.out.println("✅ User: " + SecurityContextHolder.getContext().getAuthentication().getName());
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

  @GetMapping("/accepted")
public List<AcceptedRequestDto> getAcceptedSkillPartners() {
    return exchangeService.getAcceptedSkillPartnersForCurrentUser();
}
@GetMapping("/requests/sent")
public ResponseEntity<List<SentRequestDto>> getSentRequests() {
    return ResponseEntity.ok(exchangeService.getSentRequests());
}




}
