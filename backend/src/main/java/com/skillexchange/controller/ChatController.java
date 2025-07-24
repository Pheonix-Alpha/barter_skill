package com.skillexchange.controller;

import com.skillexchange.dto.ChatMessageDto;
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
public ChatMessageDto sendMessage(@PathVariable Long receiverId, @RequestBody SendMessageRequest request) {
    User sender = getCurrentUser();
    User receiver = userRepo.findById(receiverId).orElseThrow();
    ChatMessage message = chatService.sendMessage(sender, receiver, request.getMessage());
    return new ChatMessageDto(message);  // ✅ return safe DTO
}


    // ✅ Get paginated chat history
 @GetMapping("/with/{otherUserId}")
public List<ChatMessageDto> getChatWithUser(@PathVariable Long otherUserId,
                                            @RequestParam(defaultValue = "20") int limit,
                                            @RequestParam(defaultValue = "0") int offset) {
    User currentUser = getCurrentUser();
   User otherUser = userRepo.findById(otherUserId).orElseThrow();
List<ChatMessage> messages = chatService.getChatHistory(currentUser, otherUser, limit, offset);


    return messages.stream()
                   .map(ChatMessageDto::new) // Convert each ChatMessage to DTO
                   .toList(); // or .collect(Collectors.toList()) in older Java
}

}
