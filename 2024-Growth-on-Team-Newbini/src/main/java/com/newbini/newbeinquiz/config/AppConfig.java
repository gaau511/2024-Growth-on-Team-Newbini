package com.newbini.newbeinquiz.config;

import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Value("${openai.api.key}")
    private String key;

    @Bean
    public OpenAiAudioApi openAiAudioApi() {
        return new OpenAiAudioApi(key);
    }
}
