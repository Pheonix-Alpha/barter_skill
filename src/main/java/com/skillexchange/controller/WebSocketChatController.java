package com.skillexchange.controller;

import com.skillexchange.dto.ChatMessageDto;
import com.skillexchange.model.ChatMessage;
import com.skillexchange.model.User;
import com.skillexchange.repository.ChatMessageRepository;
import com.skillexchange.repository.UserRepository;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
public class WebSocketChatController {

    private final UserRepository userRepo;
    private final ChatMessageRepository chatRepo;

    public WebSocketChatController(UserRepository userRepo, ChatMessageRepository chatRepo) {
        this.userRepo = userRepo;
        this.chatRepo = chatRepo;
    }

    @MessageMapping("/chat")  // client sends to /app/chat
    @SendTo("/topic/messages") // server sends to /topic/messages
    public ChatMessageDto send(ChatMessageDto messageDto) {
        // Save to DB
        User sender = userRepo.findByUsername(messageDto.getSender()).orElseThrow();
        User receiver = userRepo.findByUsername(messageDto.getReceiver()).orElseThrow();

        ChatMessage saved = ChatMessage.builder()
                .sender(sender)
                .receiver(receiver)
                .message(messageDto.getContent())
                .timestamp(LocalDateTime.now())
                .build();

        chatRepo.save(saved);
        return messageDto;
    }
}
