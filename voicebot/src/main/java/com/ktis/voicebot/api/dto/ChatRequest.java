package com.ktis.voicebot.api.dto;

import com.ktis.voicebot.core.model.UiContext;

import jakarta.validation.constraints.NotBlank;

public class ChatRequest {

    @NotBlank
    private String sessionId;

    @NotBlank
    private String text;

    // optional (TV UI 컨텍스트)
    private UiContext uiContext;

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public UiContext getUiContext() { return uiContext; }
    public void setUiContext(UiContext uiContext) { this.uiContext = uiContext; }
}