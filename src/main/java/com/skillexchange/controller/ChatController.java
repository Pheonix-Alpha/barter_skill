package com.skillexchange.controller;

import com.skillexchange.model.ChatMessage;
import com.skillexchange.model.User;
import com.skillexchange.repository.ChatMessageRepository;
import com.skillexchange.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatMessageRepository chatRepo;
    private final UserRepository userRepo;

    public ChatController(ChatMessageRepository chatRepo, UserRepository userRepo) {
        this.chatRepo = chatRepo;
        this.userRepo = userRepo;
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepo.findByUsername(username).orElseThrow();
    }

    @PostMapping("/send/{receiverId}")
    public ChatMessage sendMessage(@PathVariable Long receiverId, @RequestBody String message) {
        User sender = getCurrentUser();
        User receiver = userRepo.findById(receiverId).orElseThrow();

        ChatMessage chat = ChatMessage.builder()
                .sender(sender)
                .receiver(receiver)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();

        return chatRepo.save(chat);
    }

    @GetMapping("/with/{userId}")
    public List<ChatMessage> getChatWithUser(@PathVariable Long userId) {
        User me = getCurrentUser();
        User other = userRepo.findById(userId).orElseThrow();

        List<ChatMessage> sent = chatRepo.findBySenderAndReceiver(me, other);
        List<ChatMessage> received = chatRepo.findBySenderAndReceiver(other, me);

        sent.addAll(received);
        sent.sort((a, b) -> a.getTimestamp().compareTo(b.getTimestamp())); // sort chronologically

        return sent;
    }
}
