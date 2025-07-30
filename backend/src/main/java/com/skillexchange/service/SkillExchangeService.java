package com.skillexchange.service;

import com.skillexchange.dto.AcceptedRequestDto;
import com.skillexchange.dto.SentRequestDto;
import com.skillexchange.dto.SkillRequestDto;
import com.skillexchange.dto.SkillResponseDto;
import com.skillexchange.dto.UserDTO;
import com.skillexchange.exception.ResourceNotFoundException;
import com.skillexchange.model.*;
import com.skillexchange.repository.LearningSessionRepository;
import com.skillexchange.repository.SkillExchangeRequestRepository;
import com.skillexchange.repository.SkillRepository;
import com.skillexchange.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SkillExchangeService {

    private final SkillExchangeRequestRepository requestRepo;
    private final SkillRepository skillRepo;
    private final UserRepository userRepo;
    private final LearningSessionRepository sessionRepo;

    public SkillExchangeService(SkillExchangeRequestRepository requestRepo,
                                SkillRepository skillRepo,
                                UserRepository userRepo,
                                LearningSessionRepository sessionRepo) {
        this.requestRepo = requestRepo;
        this.skillRepo = skillRepo;
        this.userRepo = userRepo;
        this.sessionRepo = sessionRepo;
    }

    private User getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            throw new RuntimeException("No authenticated user found.");
        }

        return userRepo.findByUsername(auth.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + auth.getName()));
    }

    @Transactional
    public SkillResponseDto createRequest(SkillRequestDto dto) {
        User requester = getCurrentUser();
        User target = userRepo.findById(dto.getTargetUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Target user not found"));

        Skill offeredSkill = skillRepo.findById(dto.getOfferedSkillId())
                .orElseThrow(() -> new ResourceNotFoundException("Offered skill not found"));
        Skill wantedSkill = skillRepo.findById(dto.getWantedSkillId())
                .orElseThrow(() -> new ResourceNotFoundException("Wanted skill not found"));

        SkillType type = SkillType.valueOf(dto.getType().toUpperCase());

       boolean exists = requestRepo.existsAcceptedExchangeBetweenUsersAndSkill(
        requester.getId(), target.getId(), 
        type == SkillType.WANTED ? wantedSkill.getId() : offeredSkill.getId());


        if (exists) {
            throw new IllegalStateException("A pending request already exists for this skill and user.");
        }

        SkillExchangeRequest saved = requestRepo.save(SkillExchangeRequest.builder()
                .requester(requester)
                .target(target)
                .wantedSkill(wantedSkill)
                .offeredSkill(offeredSkill)
                .type(type)
                .status(RequestStatus.PENDING)
                .build());

        return mapToDto(saved);
    }

    @Transactional(readOnly = true)
    public List<SkillResponseDto> getMyRequests() {
        User currentUser = getCurrentUser();
        List<SkillExchangeRequest> sent = requestRepo.findByRequester(currentUser);
        List<SkillExchangeRequest> received = requestRepo.findByTarget(currentUser);
        sent.addAll(received);

        return sent.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SkillResponseDto> getAcceptedRequests() {
        User currentUser = getCurrentUser();
        List<SkillExchangeRequest> acceptedRequests =
                requestRepo.findByStatusAndSenderOrReceiver(RequestStatus.ACCEPTED, currentUser.getId());

        return acceptedRequests.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AcceptedRequestDto> getAcceptedSkillPartnersForCurrentUser() {
        User currentUser = getCurrentUser();
        List<SkillExchangeRequest> acceptedRequests =
                requestRepo.findByStatusAndSenderOrReceiver(RequestStatus.ACCEPTED, currentUser.getId());

        return acceptedRequests.stream()
                .map(req -> {
                    User partner = req.getRequester().equals(currentUser)
                            ? req.getTarget()
                            : req.getRequester();

                    Skill skill = req.getType() == SkillType.WANTED ? req.getWantedSkill() : req.getOfferedSkill();

                    return new AcceptedRequestDto(
                            partner.getId(),
                            partner.getUsername(),
                            skill.getId(),
                            skill.getName()
                    );
                })
                .distinct()
                .collect(Collectors.toList());
    }

    @Transactional
    public SkillResponseDto respondToRequest(Long requestId, RequestStatus newStatus) {
        SkillExchangeRequest request = requestRepo.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found with id: " + requestId));

        request.setStatus(newStatus);
        return mapToDto(request);
    }
private SkillResponseDto mapToDto(SkillExchangeRequest req) {
    User currentUser = getCurrentUser(); // Needed for UserDTO logic

    String type = req.getType() != null ? req.getType().name() : null;

    return new SkillResponseDto(
        req.getId(),
        type,
        req.getStatus(),
        new UserDTO(req.getRequester(), currentUser),
        new UserDTO(req.getTarget(), currentUser),
        req.getOfferedSkill() != null ? req.getOfferedSkill().getName() : null,
        req.getWantedSkill() != null ? req.getWantedSkill().getName() : null,
        currentUser.getId()
    );
}
@Transactional(readOnly = true)
public List<SentRequestDto> getSentRequests() {
    User currentUser = getCurrentUser();
    List<SkillExchangeRequest> requests = requestRepo.findByRequester(currentUser);

    return requests.stream()
            .filter(req -> req.getStatus() == RequestStatus.PENDING)
            .map(req -> {
                Long skillId = req.getType() == SkillType.WANTED
                        ? req.getWantedSkill().getId()
                        : req.getOfferedSkill().getId();
                return new SentRequestDto(req.getTarget().getId(), skillId, req.getType());
            })
            .collect(Collectors.toList());
}




    private void createLearningSessionFromRequest(SkillExchangeRequest request) {
        LearningSession session = new LearningSession();

        if (request.getType() == SkillType.WANTED) {
            session.setLearner(request.getRequester());
            session.setTeacher(request.getTarget());
            session.setTopic(request.getWantedSkill().getName());
        } else if (request.getType() == SkillType.OFFERED) {
            session.setLearner(request.getTarget());
            session.setTeacher(request.getRequester());
            session.setTopic(request.getOfferedSkill().getName());
        }

        session.setScheduledAt(LocalDateTime.now().plusDays(1).withHour(17).withMinute(0).withSecond(0).withNano(0));
        sessionRepo.save(session);
    }
}
