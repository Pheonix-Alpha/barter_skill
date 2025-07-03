package com.skillexchange.service;

import com.skillexchange.dto.SkillExchangeRequestDto;
import com.skillexchange.model.*;
import com.skillexchange.repository.SkillExchangeRequestRepository;
import com.skillexchange.repository.SkillRepository;
import com.skillexchange.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillExchangeService {

    private final SkillExchangeRequestRepository requestRepo;
    private final SkillRepository skillRepo;
    private final UserRepository userRepo;

    public SkillExchangeService(SkillExchangeRequestRepository requestRepo, SkillRepository skillRepo, UserRepository userRepo) {
        this.requestRepo = requestRepo;
        this.skillRepo = skillRepo;
        this.userRepo = userRepo;
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepo.findByUsername(username).orElseThrow();
    }

    public SkillExchangeRequest createRequest(SkillExchangeRequestDto dto) {
        User requester = getCurrentUser();
        User target = userRepo.findById(dto.getTargetUserId()).orElseThrow();
        Skill skill = skillRepo.findById(dto.getSkillId()).orElseThrow();

        SkillExchangeRequest request = SkillExchangeRequest.builder()
                .requester(requester)
                .target(target)
                .skill(skill)
                .status(RequestStatus.PENDING)
                .build();

        return requestRepo.save(request);
    }

    public List<SkillExchangeRequest> getMyRequests() {
        User currentUser = getCurrentUser();
        List<SkillExchangeRequest> sent = requestRepo.findByRequester(currentUser);
        List<SkillExchangeRequest> received = requestRepo.findByTarget(currentUser);
        sent.addAll(received);
        return sent;
    }

    public SkillExchangeRequest respondToRequest(Long id, RequestStatus newStatus) {
        SkillExchangeRequest req = requestRepo.findById(id).orElseThrow();
        req.setStatus(newStatus);
        return requestRepo.save(req);
    }
}
