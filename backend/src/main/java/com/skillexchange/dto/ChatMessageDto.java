package com.skillexchange.dto;

import com.skillexchange.model.ChatMessage;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ChatMessageDto {
    private String sender;
    private String receiver;
    private String content;
    private LocalDateTime timestamp;

    public ChatMessageDto(ChatMessage message) {
        this.sender = message.getSender().getUsername();      // ✅ avoid full User object
        this.receiver = message.getReceiver().getUsername();  // ✅
        this.content = message.getMessage();                  // ✅ get message content
        this.timestamp = message.getTimestamp();
    }
}
