package com.newbini.quizard.client;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Map;

@Slf4j
public class ListAssistantsTest {

    TestRestTemplate restTemplate = new TestRestTemplate();

    private String apiKey = "possible_key";

    @Test
    void listAssistants() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);
        headers.add("OpenAI-Beta", "assistants=v2");

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "https://api.openai.com/v1/assistants?order=desc&limit=2", // 요청할 URL
                HttpMethod.GET,  // HTTP 메소드
                requestEntity,
                String.class
        );
        log.info("listAssistantsTest response = {}", response);
    }
}
