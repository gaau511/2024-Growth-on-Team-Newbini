package com.newbini.quizard.client.chatgpt.assistant;

import com.newbini.quizard.client.chatgpt.common.OpenAIBasicHeaderConst;
import com.newbini.quizard.dto.request.ChatGptAssistantResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static com.newbini.quizard.client.chatgpt.common.InstructionConst.*;

@Component
@RequiredArgsConstructor
public class ChatGptClient {

    @Value("${openai.api.key}")
    private String openAiApiKey;

    private final RestTemplate restTemplate;

    public ChatGptAssistantResponse createAssistant() {
        HttpHeaders header = getBasicHeader();

        Map<String, Object> body = new HashMap<>();
        body.put("name", NAME);
        body.put("model", MODEL);
        body.put("tools", TOOLS);
        body.put("temperature", TEMPERATURE);
        body.put("instructions", INSTRUCTIONS);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, header);

        return restTemplate.exchange(
                ASSISTANT_BASE_URL,
                HttpMethod.POST,
                requestEntity,
                ChatGptAssistantResponse.class
        ).getBody();

    }

    private HttpHeaders getBasicHeader() {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        header.setBearerAuth(openAiApiKey);
        header.add("OpenAI-Beta", "assistants=v2");

        return header;
    }
}
