package com.skillexchange.service;

import com.skillexchange.model.ChatMessage;
import com.skillexchange.model.User;
import com.skillexchange.repository.ChatMessageRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatMessageService {

    private final ChatMessageRepository repo;

    public ChatMessageService(ChatMessageRepository repo) {
        this.repo = repo;
    }

    public ChatMessage sendMessage(User sender, User receiver, String message) {
        if (sender == null || receiver == null || message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException("Sender, receiver, and message must be non-null.");
        }

        ChatMessage chat = ChatMessage.builder()
                .sender(sender)
                .receiver(receiver)
                .message(message.trim())
                .timestamp(LocalDateTime.now())
                .build();

        return repo.save(chat);
    }

    public List<ChatMessage> getChatHistory(User user1, User user2, int limit, int offset) {
        Pageable pageable = PageRequest.of(offset / limit, limit); // page = offset / limit
        return repo.findChatHistory(user1, user2, pageable);
    }
}
