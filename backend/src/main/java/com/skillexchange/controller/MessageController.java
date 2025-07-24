package com.skillexchange.controller;

import com.skillexchange.dto.ConversationDTO;
import com.skillexchange.repository.MessageRepository;
import com.skillexchange.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private JwtService jwtService;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @GetMapping("/conversations")
    public ResponseEntity<List<ConversationDTO>> getUserConversations(
            @RequestHeader("Authorization") String tokenHeader) {

        // Remove Bearer prefix
        String token = tokenHeader.replace("Bearer ", "");

        // Parse claims to extract userId
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtSecret.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();

        Long userId = claims.get("userId", Long.class);

        List<ConversationDTO> conversations = messageRepository.findUserConversations(userId);
        return ResponseEntity.ok(conversations);
    }
}
