package com.skillexchange.repository;

import com.skillexchange.model.ChatMessage;
import com.skillexchange.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findBySenderAndReceiver(User sender, User receiver);
    List<ChatMessage> findByReceiverAndSender(User receiver, User sender);
}
