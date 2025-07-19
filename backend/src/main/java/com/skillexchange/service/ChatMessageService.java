package com.skillexchange.service;

import com.skillexchange.model.ChatMessage;
import com.skillexchange.model.User;
import com.skillexchange.repository.ChatMessageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChatMessageService {

    private final ChatMessageRepository repo;

    public ChatMessageService(ChatMessageRepository repo) {
        this.repo = repo;
    }

    public ChatMessage sendMessage(User sender, User receiver, String message) {
        ChatMessage chat = ChatMessage.builder()
                .sender(sender)
                .receiver(receiver)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();

        return repo.save(chat);
    }

    public List<ChatMessage> getChatHistory(User me, User other) {
        List<ChatMessage> sent = repo.findBySenderAndReceiver(me, other);
        List<ChatMessage> received = repo.findBySenderAndReceiver(other, me);

        List<ChatMessage> all = new ArrayList<>();
        all.addAll(sent);
        all.addAll(received);

        all.sort((a, b) -> a.getTimestamp().compareTo(b.getTimestamp()));
        return all;
    }
}
