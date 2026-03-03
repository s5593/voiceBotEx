package com.ktis.voicebot.config;

import com.ktis.voicebot.core.model.SessionData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, SessionData> sessionRedisTemplate(RedisConnectionFactory cf) {
        RedisTemplate<String, SessionData> t = new RedisTemplate<>();
        t.setConnectionFactory(cf);

        StringRedisSerializer keySer = new StringRedisSerializer();
        JacksonJsonRedisSerializer<SessionData> valSer = new JacksonJsonRedisSerializer<>(SessionData.class);

        t.setKeySerializer(keySer);
        t.setValueSerializer(valSer);
        t.setHashKeySerializer(keySer);
        t.setHashValueSerializer(valSer);

        t.afterPropertiesSet();
        return t;
    }
}