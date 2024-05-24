package com.newbini.newbeinquiz.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newbini.newbeinquiz.web.response.AssistantObject;
import com.newbini.newbeinquiz.web.response.ThreadObject;
import com.newbini.newbeinquiz.web.response.VectorStoreObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.*;

public class AssistantGenerator {

    private final String openAiApiKey;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private String assistant_id = "";
    private String thread_id = "";
    private String vector_store_id = "";

    public AssistantGenerator(String openAiApiKey) {
        this.openAiApiKey = openAiApiKey;
    }

    public AssistantObject createAssistant() throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openAiApiKey);
        headers.add("OpenAI-Beta", "assistants=v2");

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("instructions", "You will make quizzes based on given files.");
        requestBody.put("name", "Quizard");

        List<Map<String, String>> toolsList = Collections.singletonList(Map.of("type", "file_search"));
        requestBody.put("tools", toolsList);
        requestBody.put("model", "gpt-4o-2024-05-13");

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        String response = restTemplate.postForObject("https://api.openai.com/v1/assistants", requestEntity, String.class);
        AssistantObject assistant = objectMapper.readValue(response, AssistantObject.class);
        assistant_id = assistant.getId();

        return assistant;
    }

    public ThreadObject createThread() throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openAiApiKey);
        headers.add("OpenAI-Beta", "assistants=v2");

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(headers);
        String response = restTemplate.postForObject("https://api.openai.com/v1/threads", requestEntity, String.class);
        ThreadObject thread = objectMapper.readValue(response, ThreadObject.class);
        thread_id = thread.getId();

        return thread;
    }

    public VectorStoreObject createVectorStore() throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openAiApiKey);
        headers.add("OpenAI-Beta", "assistants=v2");

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "Quiz Resources Storage");

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        String response = restTemplate.postForObject("https://api.openai.com/v1/vector_stores", requestEntity, String.class);
        VectorStoreObject vectorStore = objectMapper.readValue(response, VectorStoreObject.class);
        vector_store_id = vectorStore.getId();

        return vectorStore;
    }

}
