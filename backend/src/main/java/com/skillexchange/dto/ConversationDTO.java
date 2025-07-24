package com.skillexchange.dto;

import java.time.LocalDateTime;

public class ConversationDTO {
    private Long userId;
    private String username;
    private LocalDateTime timestamp;

    public ConversationDTO(Long userId, String username, LocalDateTime timestamp) {
        this.userId = userId;
        this.username = username;
        this.timestamp = timestamp;
    }

    // ✅ Getters
    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    // ✅ Setters
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
