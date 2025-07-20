package com.skillexchange.repository;

import com.skillexchange.model.ChatMessage;
import com.skillexchange.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findBySenderAndReceiver(User sender, User receiver);

    List<ChatMessage> findByReceiverAndSender(User receiver, User sender);

    @Query("SELECT m FROM ChatMessage m WHERE " +
           "(m.sender = :user1 AND m.receiver = :user2) OR " +
           "(m.sender = :user2 AND m.receiver = :user1) " +
           "ORDER BY m.timestamp ASC")
    List<ChatMessage> findChatHistory(@Param("user1") User user1,
                                      @Param("user2") User user2,
                                      Pageable pageable);
}
