package com.ktis.voicebot.api;

import org.springframework.web.bind.annotation.*;

import com.ktis.voicebot.api.dto.ChatRequest;
import com.ktis.voicebot.api.dto.ChatResponse;
import com.ktis.voicebot.core.ChatService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/chat")
    public ChatResponse handle(@Valid @RequestBody ChatRequest req) {
        return chatService.handle(req);
    }
}