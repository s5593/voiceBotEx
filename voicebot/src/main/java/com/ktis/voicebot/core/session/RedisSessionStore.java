package com.ktis.voicebot.core.session;

import com.ktis.voicebot.core.model.SessionData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;

@Component
public class RedisSessionStore implements SessionStore {

    private final RedisTemplate<String, SessionData> redis;
    private final Duration ttl;

    public RedisSessionStore(
            RedisTemplate<String, SessionData> sessionRedisTemplate,
            @Value("${bot.session.ttl-seconds:600}") long ttlSeconds
    ) {
        this.redis = sessionRedisTemplate;
        this.ttl = Duration.ofSeconds(ttlSeconds);
    }

    private String key(String sessionId) {
        return "session:" + sessionId;
    }

    @Override
    public Optional<SessionData> get(String sessionId) {
        SessionData v = redis.opsForValue().get(key(sessionId));
        return Optional.ofNullable(v);
    }

    @Override
    public SessionData getOrCreate(String sessionId) {
    	System.out.println("[SESSION] getOrCreate sessionId=" + sessionId);
    	
        return get(sessionId).orElseGet(() -> new SessionData(sessionId));
    }

    @Override
    public void save(SessionData session) {
    	System.out.println("[SESSION] save sessionId=" + session.getSessionId() + ", state=" + session.getState());
    	
        session.setUpdatedAtEpochMs(System.currentTimeMillis());
        redis.opsForValue().set(key(session.getSessionId()), session, ttl);
    }
}