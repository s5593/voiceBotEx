package com.ktis.voicebot.api.dto;

import jakarta.validation.constraints.NotBlank;

public class ChatRequest {
    @NotBlank
    private String sessionId;

    @NotBlank
    private String text;

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}