package com.newbini.quizard.client;

import com.newbini.quizard.dto.response.ListAssistantsObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class DeleteAssistantsTest {

    TestRestTemplate restTemplate = new TestRestTemplate();

    private String apiKey = "possible_key";

    @Test
    void delete100Assistants() {

        //list assistants
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);
        headers.add("OpenAI-Beta", "assistants=v2");

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<ListAssistantsObject> response = restTemplate.exchange(
                "https://api.openai.com/v1/assistants?order=desc&limit=100", // 요청할 URL
                HttpMethod.GET,  // HTTP 메소드
                requestEntity,
                ListAssistantsObject.class
        );

        List<String> assistantsIdList = new ArrayList<>();
        response.getBody().getData().stream().forEach(m -> assistantsIdList.add(m.getId()));

        log.info("assistantsIdList = {}", assistantsIdList);

        for (String id : assistantsIdList) {
            ResponseEntity<String> deleteResponse = restTemplate.exchange(
                    "https://api.openai.com/v1/assistants/"+id, // 요청할 URL
                    HttpMethod.DELETE,  // HTTP 메소드
                    requestEntity,
                    String.class
            );

            if (!deleteResponse.getStatusCode().is2xxSuccessful()) {
                log.info("delete unsuccesed status code = {}", deleteResponse.getStatusCode());
            }
        }
    }
}
