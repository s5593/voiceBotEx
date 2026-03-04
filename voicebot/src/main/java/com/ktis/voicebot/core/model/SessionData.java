package com.ktis.voicebot.core.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SessionData {
    private String sessionId;
    private ConversationState state = ConversationState.START;

    // 기존 호환을 위해 유지 (일상대화에서는 안 써도 됨)
    private Map<String, String> slots = new HashMap<>();

    private long updatedAtEpochMs;

    // 일상 대화용 대화 히스토리
    private List<ChatTurn> history = new ArrayList<>();

    public SessionData() {}

    public SessionData(String sessionId) {
        this.sessionId = sessionId;
        this.updatedAtEpochMs = System.currentTimeMillis();

        // 기존 코드 호환용(원하면 지워도 되지만 기존 흐름 안 깨지게 유지)
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