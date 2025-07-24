package com.skillexchange.repository;

import com.skillexchange.dto.ConversationDTO;
import com.skillexchange.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query("""
        SELECT new com.skillexchange.dto.ConversationDTO(
            CASE 
                WHEN m.sender.id = :userId THEN m.receiver.id
                ELSE m.sender.id
            END,
            CASE 
                WHEN m.sender.id = :userId THEN m.receiver.username
                ELSE m.sender.username
            END,
            MAX(m.timestamp)
        )
        FROM ChatMessage m
        WHERE m.sender.id = :userId OR m.receiver.id = :userId
        GROUP BY 
            CASE 
                WHEN m.sender.id = :userId THEN m.receiver.id
                ELSE m.sender.id
            END,
            CASE 
                WHEN m.sender.id = :userId THEN m.receiver.username
                ELSE m.sender.username
            END
        ORDER BY MAX(m.timestamp) DESC
    """)
    List<ConversationDTO> findUserConversations(@Param("userId") Long userId);
}
