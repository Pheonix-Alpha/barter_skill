package com.skillexchange.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ChatMessageDto {
    private String sender;
    private String receiver;
    private String content;
    private LocalDateTime timestamp;
}
