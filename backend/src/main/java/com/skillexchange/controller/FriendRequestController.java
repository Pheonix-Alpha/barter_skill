package com.skillexchange.controller;

import com.skillexchange.dto.UserDTO;
import com.skillexchange.model.User;
import com.skillexchange.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/api/friends")
public class FriendRequestController {

    private final UserRepository userRepo;

    public FriendRequestController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    // ✅ Send Friend Request
    @PostMapping("/request/{receiverId}")
    @Transactional
    public String sendRequest(@PathVariable Long receiverId, Authentication auth) {
        try {
            User sender = userRepo.findWithRelationsByUsername(auth.getName())
                    .orElseThrow(() -> new RuntimeException("Sender not found: " + auth.getName()));

            User receiver = userRepo.findWithRelationsById(receiverId)
                    .orElseThrow(() -> new RuntimeException("Receiver not found: ID " + receiverId));

            System.out.println("Sender: " + sender.getId() + " | Receiver: " + receiver.getId());

            if (sender.equals(receiver)) {
                return "❌ Cannot send friend request to yourself.";
            }

            if (sender.getSentRequests().contains(receiver)) {
                return "❌ Friend request already sent.";
            }

            if (sender.getFriends().contains(receiver)) {
                return "❌ You are already friends.";
            }

            sender.getSentRequests().add(receiver);
            receiver.getReceivedRequests().add(sender);

            userRepo.save(sender);
            userRepo.save(receiver);

            return "✅ Friend request sent successfully.";
        } catch (Exception e) {
            e.printStackTrace();
            return "❌ Failed to send friend request: " + e.getMessage();
        }
    }

    // ✅ Accept Friend Request
    @PostMapping("/accept/{senderId}")
    @Transactional
    public String acceptRequest(@PathVariable Long senderId, Authentication auth) {
        try {
            User receiver = userRepo.findWithRelationsByUsername(auth.getName())
                    .orElseThrow(() -> new RuntimeException("Receiver not found: " + auth.getName()));

            User sender = userRepo.findWithRelationsById(senderId)
                    .orElseThrow(() -> new RuntimeException("Sender not found: ID " + senderId));

            if (!receiver.getReceivedRequests().contains(sender)) {
                return "❌ No friend request to accept.";
            }

            receiver.getReceivedRequests().remove(sender);
            sender.getSentRequests().remove(receiver);

            receiver.getFriends().add(sender);
            sender.getFriends().add(receiver);

            userRepo.save(receiver);
            userRepo.save(sender);

            return "✅ Friend request accepted.";
        } catch (Exception e) {
            e.printStackTrace();
            return "❌ Failed to accept request: " + e.getMessage();
        }
    }

    // ✅ Reject Friend Request
    @PostMapping("/reject/{senderId}")
    @Transactional
    public String rejectRequest(@PathVariable Long senderId, Authentication auth) {
        try {
            User receiver = userRepo.findWithRelationsByUsername(auth.getName())
                    .orElseThrow(() -> new RuntimeException("Receiver not found: " + auth.getName()));

            User sender = userRepo.findWithRelationsById(senderId)
                    .orElseThrow(() -> new RuntimeException("Sender not found: ID " + senderId));

            receiver.getReceivedRequests().remove(sender);
            sender.getSentRequests().remove(receiver);

            userRepo.save(receiver);
            userRepo.save(sender);

            return "✅ Friend request rejected.";
        } catch (Exception e) {
            e.printStackTrace();
            return "❌ Failed to reject request: " + e.getMessage();
        }
    }

    // ✅ Get all received requests
    @GetMapping("/received")
    @Transactional
    public List<UserDTO> getReceivedRequests(Authentication auth) {
        User me = userRepo.findWithRelationsByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return me.getReceivedRequests()
                .stream()
                .map(user -> new UserDTO(user, me))

                .collect(Collectors.toList());
    }

    // ✅ Get all sent requests
    @GetMapping("/sent")
    @Transactional
    public List<UserDTO> getSentRequests(Authentication auth) {
        User me = userRepo.findWithRelationsByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return me.getSentRequests()
                .stream()
                .map(user -> new UserDTO(user, me))

                .collect(Collectors.toList());
    }

    // ✅ Get all accepted friends
    @GetMapping("/list")
    @Transactional
    public List<UserDTO> getFriends(Authentication auth) {
        User me = userRepo.findWithRelationsByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return me.getFriends()
                .stream()
                .map(user -> new UserDTO(user, me))

                .collect(Collectors.toList());
    }
}
