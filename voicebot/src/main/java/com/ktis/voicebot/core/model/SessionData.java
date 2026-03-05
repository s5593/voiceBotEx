package com.ktis.voicebot.core.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SessionData {
    private String sessionId;
    private ConversationState state = ConversationState.START;

    private Map<String, String> slots = new HashMap<String, String>();

    private long updatedAtEpochMs;

    private List<ChatTurn> history = new ArrayList<ChatTurn>();

    // TV/화면 컨텍스트
    private UiContext uiContext;

    public SessionData() {}

    public SessionData(String sessionId) {
        this.sessionId = sessionId;
        this.updatedAtEpochMs = System.currentTimeMillis();

        this.slots.put("accountNumber", null);
        this.slots.put("amount", null);
    }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public ConversationState getState() { return state; }
    public void setState(ConversationState state) { this.state = state; }

    public Map<String, String> getSlots() { return slots; }
    public void setSlots(Map<String, String> slots) { this.slots = slots; }

    public long getUpdatedAtEpochMs() { return updatedAtEpochMs; }
    public void setUpdatedAtEpochMs(long updatedAtEpochMs) { this.updatedAtEpochMs = updatedAtEpochMs; }

    public List<ChatTurn> getHistory() { return history; }
    public void setHistory(List<ChatTurn> history) { this.history = history; }

    public UiContext getUiContext() { return uiContext; }
    public void setUiContext(UiContext uiContext) { this.uiContext = uiContext; }

    public static class ChatTurn {
        private String role;     // user / assistant
        private String content;

        public ChatTurn() {}
        public ChatTurn(String role, String content) {
            this.role = role;
            this.content = content;
        }

        public static ChatTurn user(String content) { return new ChatTurn("user", content); }
        public static ChatTurn assistant(String content) { return new ChatTurn("assistant", content); }

        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }

        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }
}