package com.skillexchange.controller;

import com.skillexchange.dto.UserDTO;
import com.skillexchange.service.FriendRequestService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/api/friends")
public class FriendRequestController {

    private final FriendRequestService friendRequestService;

    public FriendRequestController(FriendRequestService friendRequestService) {
        this.friendRequestService = friendRequestService;
    }

    @PostMapping("/request/{receiverId}")
    public String sendRequest(@PathVariable Long receiverId, Authentication auth) {
        return friendRequestService.sendRequest(auth.getName(), receiverId);
    }

    @PostMapping("/accept/{senderId}")
    public String acceptRequest(@PathVariable Long senderId, Authentication auth) {
        return friendRequestService.acceptRequest(auth.getName(), senderId);
    }

    @PostMapping("/reject/{senderId}")
    public String rejectRequest(@PathVariable Long senderId, Authentication auth) {
        return friendRequestService.rejectRequest(auth.getName(), senderId);
    }

    @GetMapping("/received")
    public List<UserDTO> getReceivedRequests(Authentication auth) {
        return friendRequestService.getReceivedRequests(auth.getName());
    }

    @GetMapping("/sent")
    public List<UserDTO> getSentRequests(Authentication auth) {
        return friendRequestService.getSentRequests(auth.getName());
    }

    @GetMapping("/list")
    public List<UserDTO> getFriends(Authentication auth) {
        return friendRequestService.getFriends(auth.getName());
    }
}
