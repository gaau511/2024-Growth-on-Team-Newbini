package com.newbini.quizard.client.chatgpt.assistant;

import com.newbini.quizard.dto.response.DeleteAssistantObject;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class AssistantDeleteManager {
    private final RestTemplate restTemplate;
    private final String openAiApiKey;


    public DeleteAssistantObject deleteAssistant(String assistant_id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openAiApiKey);
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
