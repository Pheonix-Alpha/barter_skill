package com.skillexchange.service;

import com.skillexchange.dto.SkillRequestDto;
import com.skillexchange.dto.SkillResponseDto;
import com.skillexchange.exception.ResourceNotFoundException;
import com.skillexchange.model.*;
import com.skillexchange.repository.SkillExchangeRequestRepository;
import com.skillexchange.repository.SkillRepository;
import com.skillexchange.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SkillExchangeService {

    private final SkillExchangeRequestRepository requestRepo;
    private final SkillRepository skillRepo;
    private final UserRepository userRepo;

    public SkillExchangeService(
            SkillExchangeRequestRepository requestRepo,
            SkillRepository skillRepo,
            UserRepository userRepo) {
        this.requestRepo = requestRepo;
        this.skillRepo = skillRepo;
        this.userRepo = userRepo;
    }

  private User getCurrentUser() {
    var auth = SecurityContextHolder.getContext().getAuthentication();
    System.out.println("🔐 Auth object: " + auth);

    if (auth == null || auth.getName() == null) {
        throw new RuntimeException("No authenticated user found.");
    }

    String username = auth.getName(); // Should be actual username from JWT
    System.out.println("👤 Extracted username from SecurityContext: " + username);

    return userRepo.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
}


    /**
     * Create a new skill exchange request if not already pending.
     */
    @Transactional
    public SkillResponseDto createRequest(SkillRequestDto dto) {
        User requester = getCurrentUser();
        User target = userRepo.findById(dto.getTargetUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Target user not found"));
        Skill skill = skillRepo.findById(dto.getSkillId())
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found"));

        // Validate type
        SkillType skillType;
        try {
            skillType = SkillType.valueOf(dto.getType().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("Invalid or missing skill type: " + dto.getType());
        }

        boolean exists = requestRepo.existsByRequesterAndTargetAndSkillAndStatus(
                requester, target, skill, RequestStatus.PENDING);
        if (exists) {
            throw new IllegalStateException("A pending request already exists for this skill and user.");
        }

        SkillExchangeRequest saved = requestRepo.save(SkillExchangeRequest.builder()
                .requester(requester)
                .target(target)
                .skill(skill)
                .status(RequestStatus.PENDING)
                .build());

        // Force username access to prevent lazy loading issues
        String requesterUsername = saved.getRequester().getUsername();
        String targetUsername = saved.getTarget().getUsername();
        String skillName = saved.getSkill().getName();

       return new SkillResponseDto(
    saved.getId(),
    saved.getRequester().getUsername(),
    saved.getTarget().getUsername(),
    saved.getTarget().getId(),      // add this here
    saved.getSkill().getName(),
    saved.getStatus().name()
);

    }

    /**
     * Get all skill exchange requests (sent and received) of current user.
     */
   @Transactional(readOnly = true)
public List<SkillResponseDto> getMyRequests() {
    User currentUser = getCurrentUser();

    List<SkillExchangeRequest> sent = requestRepo.findByRequester(currentUser);
    List<SkillExchangeRequest> received = requestRepo.findByTarget(currentUser);

    sent.addAll(received);

    return sent.stream()
            .map(req -> new SkillResponseDto(
                    req.getId(),
                    req.getRequester().getUsername(),
                    req.getTarget().getUsername(),
                    req.getTarget().getId(),       // Added targetUserId here
                    req.getSkill().getName(),
                    req.getStatus().name()
            ))
            .collect(Collectors.toList());
}


    /**
     * Respond to a skill exchange request by updating its status.
     */
    @Transactional
    public SkillResponseDto respondToRequest(Long requestId, RequestStatus newStatus) {
        SkillExchangeRequest request = requestRepo.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found with id: " + requestId));

        request.setStatus(newStatus);

        // No need to call save() — changes will persist due to @Transactional

        return new SkillResponseDto(
    request.getId(),
    request.getRequester().getUsername(),
    request.getTarget().getUsername(),
    request.getTarget().getId(),       // <-- added this line
    request.getSkill().getName(),
    request.getStatus().name()
);

    }
}
