package com.ktis.voicebot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NormalizerConfig {

    @Bean
    public NormalizerProperties normalizerProperties() {
        return new NormalizerProperties();
    }
}