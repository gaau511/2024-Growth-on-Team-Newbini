package com.newbini.quizard.client.chatgpt_assistant;

import com.newbini.quizard.dto.response.ChatGptAssistantResponse;
import com.newbini.quizard.dto.response.ChatGptMessageResponse;
import com.newbini.quizard.dto.response.ChatGptRunResponse;
import com.newbini.quizard.dto.response.ChatGptThreadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static com.newbini.quizard.client.chatgpt_assistant.ChatGptAssistantConst.*;

@Component
@RequiredArgsConstructor
public class ChatGptAssistantClient {

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

    public ChatGptAssistantResponse deleteAssistant(String assistantId) {
        HttpHeaders header = getBasicHeader();

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(header);

        return restTemplate.exchange(
                ASSISTANT_BASE_URL + "/" + assistantId,
                HttpMethod.DELETE,  // HTTP 메소드
                requestEntity,
                ChatGptAssistantResponse.class
        ).getBody();
    }

    public ChatGptThreadResponse createThread() {
        HttpHeaders header = getBasicHeader();

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(header);

        return restTemplate.exchange(
                THREAD_BASE_URL,
                HttpMethod.POST,  // HTTP 메소드
                requestEntity,
                ChatGptThreadResponse.class
        ).getBody();
    }

    public ChatGptThreadResponse deleteThread(String threadId) {
        HttpHeaders header = getBasicHeader();

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(header);

        return restTemplate.exchange(
                THREAD_BASE_URL + "/" + threadId,
                HttpMethod.DELETE,
                requestEntity,
                ChatGptThreadResponse.class
        ).getBody();
    }

    public ChatGptMessageResponse createMessage(String threadId, String content, String role) {
        HttpHeaders header = getBasicHeader();

        Map<String, Object> body = new HashMap<>();
        body.put("role", role);
        body.put("content", content);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, header);

        return restTemplate.exchange(
                THREAD_BASE_URL + "/" + threadId + "/messages",
                HttpMethod.POST,
                requestEntity,
                ChatGptMessageResponse.class
        ).getBody();

    }

    public ChatGptMessageResponse listMessages(String threadId) {
        HttpHeaders header = getBasicHeader();

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(header);

        return restTemplate.exchange(
                THREAD_BASE_URL + "/" + threadId + "/messages",
                HttpMethod.GET,
                requestEntity,
                ChatGptMessageResponse.class
        ).getBody();
    }

    public ChatGptRunResponse createRun(String assistantId, String threadId) {
        HttpHeaders header = getBasicHeader();

        Map<String, Object> body = new HashMap<>();
        body.put("assistant_id", assistantId);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, header);

        return restTemplate.exchange(
                THREAD_BASE_URL + "/" + threadId + "/runs",
                HttpMethod.POST,
                requestEntity,
                ChatGptRunResponse.class
                ).getBody();
    }

    public ChatGptRunResponse retrieveRun(String threadId, String runId) {
        HttpHeaders header = getBasicHeader();

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(header);

        return restTemplate.exchange(
                THREAD_BASE_URL + "/" + threadId + "/runs/" + runId,
                HttpMethod.POST,
                requestEntity,
                ChatGptRunResponse.class
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
