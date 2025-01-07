package com.newbini.quizard.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newbini.quizard.client.chatgpt.assistant.AssistantGenerator;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Value("${openai.api.key}")
    private String key;

    @Bean(name = "openAiApiKey")
    public String getApiKey() {
        return key;
    }

    @Bean
    public AssistantGenerator assistantGenerator(RestTemplate restTemplate) {
        return new AssistantGenerator(key, restTemplate());
    }

    /**
     * Open Ai Audio Api Bean 등록
     */
    @Bean
    public OpenAiAudioApi openAiAudioApi() {
        return new OpenAiAudioApi(key);
    }

    @Bean
    public RestTemplate restTemplate() {return new RestTemplate();}

    @Bean
    public ObjectMapper objectMapper() { return new ObjectMapper();}
 }
