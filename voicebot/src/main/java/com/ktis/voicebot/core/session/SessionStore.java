package com.ktis.voicebot.core.session;

import java.util.Optional;
import com.ktis.voicebot.core.model.SessionData;

public interface SessionStore {
    Optional<SessionData> get(String sessionId);
    SessionData getOrCreate(String sessionId);
    void save(SessionData session);
}