package com.ktis.voicebot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ktis.voicebot.core.openai.OpenAiClient;

@Configuration
public class OpenAiConfig {

    @Bean
    public OpenAiClient openAiClient(OpenAiProperties props) {
        return new OpenAiClient(
                props.getApiKey(),
                props.getModel(),
                props.getTimeoutMs()
        );
    }
}