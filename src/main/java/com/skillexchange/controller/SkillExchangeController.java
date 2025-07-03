package com.skillexchange.controller;

import com.skillexchange.dto.SkillExchangeRequestDto;
import com.skillexchange.model.RequestStatus;
import com.skillexchange.model.SkillExchangeRequest;
import com.skillexchange.service.SkillExchangeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exchange")
public class SkillExchangeController {

    private final SkillExchangeService exchangeService;

    public SkillExchangeController(SkillExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    @PostMapping("/request")
    public SkillExchangeRequest requestSkill(@RequestBody SkillExchangeRequestDto dto) {
        return exchangeService.createRequest(dto);
    }

    @GetMapping("/my-requests")
    public List<SkillExchangeRequest> getRequests() {
        return exchangeService.getMyRequests();
    }

    @PutMapping("/respond/{id}")
    public SkillExchangeRequest respond(@PathVariable Long id, @RequestParam RequestStatus status) {
        return exchangeService.respondToRequest(id, status);
    }
}
