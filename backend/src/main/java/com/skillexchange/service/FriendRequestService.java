package com.skillexchange.service;

import com.skillexchange.dto.UserDTO;
import com.skillexchange.model.User;
import com.skillexchange.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendRequestService {
 
    private final UserRepository userRepo;

    public FriendRequestService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Transactional
    public String sendRequest(String senderUsername, Long receiverId) {
        User sender = userRepo.findWithRelationsByUsername(senderUsername)
                .orElseThrow(() -> new RuntimeException("Sender not found: " + senderUsername));
        User receiver = userRepo.findWithRelationsById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found: ID " + receiverId));

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
    }

    @Transactional
    public String acceptRequest(String receiverUsername, Long senderId) {
        User receiver = userRepo.findWithRelationsByUsername(receiverUsername)
                .orElseThrow(() -> new RuntimeException("Receiver not found: " + receiverUsername));
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
    }

    @Transactional
    public String rejectRequest(String receiverUsername, Long senderId) {
        User receiver = userRepo.findWithRelationsByUsername(receiverUsername)
                .orElseThrow(() -> new RuntimeException("Receiver not found: " + receiverUsername));
        User sender = userRepo.findWithRelationsById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found: ID " + senderId));

        receiver.getReceivedRequests().remove(sender);
        sender.getSentRequests().remove(receiver);

        userRepo.save(receiver);
        userRepo.save(sender);

        return "✅ Friend request rejected.";
    }

    @Transactional
    public List<UserDTO> getReceivedRequests(String username) {
        User me = userRepo.findWithRelationsByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return me.getReceivedRequests().stream()
                .map(user -> new UserDTO(user, me))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<UserDTO> getSentRequests(String username) {
        User me = userRepo.findWithRelationsByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return me.getSentRequests().stream()
                .map(user -> new UserDTO(user, me))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<UserDTO> getFriends(String username) {
        User me = userRepo.findWithRelationsByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return me.getFriends().stream()
                .map(user -> new UserDTO(user, me))
                .collect(Collectors.toList());
    }
}
