package com.skillexchange.controller;

import com.skillexchange.model.ChatMessage;
import com.skillexchange.model.User;
import com.skillexchange.payload.SendMessageRequest;
import com.skillexchange.repository.UserRepository;
import com.skillexchange.service.ChatMessageService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatMessageService chatService;
    private final UserRepository userRepo;

    public ChatController(ChatMessageService chatService, UserRepository userRepo) {
        this.chatService = chatService;
        this.userRepo = userRepo;
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepo.findByUsername(username).orElseThrow();
    }

    // ✅ Send a message using DTO
    @PostMapping("/send/{receiverId}")
    public ChatMessage sendMessage(@PathVariable Long receiverId, @RequestBody SendMessageRequest request) {
        User sender = getCurrentUser();
        User receiver = userRepo.findById(receiverId).orElseThrow();
        return chatService.sendMessage(sender, receiver, request.getMessage());
    }

    // ✅ Get paginated chat history
    @GetMapping("/with/{userId}")
    public List<ChatMessage> getChatWithUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "0") int offset
    ) {
        User me = getCurrentUser();
        User other = userRepo.findById(userId).orElseThrow();

        // ✅ Correct placement of debug log
        System.out.println("📨 Fetching chat for: " + me.getUsername() + " ↔️ " + other.getUsername());

        return chatService.getChatHistory(me, other, limit, offset);
    }
}
