package com.newbini.newbeinquiz.api;

import com.newbini.newbeinquiz.dto.response.DeleteAssistantObject;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class AssistantDeleteManager {
    RestTemplate restTemplate = new RestTemplate();

    private final String apiKey;

    public AssistantDeleteManager(String key) {
        this.apiKey = key;
    }

    public DeleteAssistantObject deleteAssistant(String assistant_id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);
        headers.add("OpenAI-Beta", "assistants=v2");

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<DeleteAssistantObject> response = restTemplate.exchange(
                "https://api.openai.com/v1/assistants/" + assistant_id, // 요청할 URL
                HttpMethod.DELETE,  // HTTP 메소드
                requestEntity,
                DeleteAssistantObject.class
        );

        return response.getBody();
    }
}
