package com.newbini.newbeinquiz.api;

import com.newbini.newbeinquiz.dto.response.ListAssistantsObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
public class ListAssistantsTest {

    TestRestTemplate restTemplate = new TestRestTemplate();

    private String apiKey = "sk-proj-TCl0PADVZBOfOk8dRjSNT3BlbkFJzl62k0eGjBAfRI5DZ64I";

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
