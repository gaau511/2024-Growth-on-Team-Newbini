package com.newbini.newbeinquiz.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newbini.newbeinquiz.config.AppConfig;
import com.newbini.newbeinquiz.dto.response.AssistantObject;
import com.newbini.newbeinquiz.dto.response.DeleteAssistantObject;
import com.newbini.newbeinquiz.dto.response.ThreadObject;
import com.newbini.newbeinquiz.dto.response.VectorStoreObject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

// TODO 무상태로 설계하기
@Component
@RequiredArgsConstructor
public class AssistantGenerator {

    private final String openAiApiKey;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    HttpHeaders headers = OpenAIBasicHeaderConst.basicHeader(openAiApiKey);

    public AssistantObject createAssistant(String type, String difficulty) {

        HttpHeaders headers = OpenAIBasicHeaderConst.basicHeader(openAiApiKey);
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("instructions", AssistantInstructionConst.createInstruction(type, difficulty));
        requestBody.put("name", "Quizard");

        List<Map<String, Object>> toolsList = new ArrayList<>();
        Map<String,Object> toolsListFileSearch =new LinkedHashMap<>();

        toolsListFileSearch.put("type","file_search");

        toolsList.add(toolsListFileSearch);
        requestBody.put("tools", toolsList);

        requestBody.put("model", "gpt-4o-2024-05-13");
        requestBody.put("temperature", 0.6);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        return restTemplate.exchange(
                "https://api.openai.com/v1/assistants",
                HttpMethod.POST,  // HTTP 메소드
                requestEntity,
                AssistantObject.class
        ).getBody();

    }

    public ThreadObject createThread() {
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(
                "https://api.openai.com/v1/threads",
                HttpMethod.POST,
                requestEntity,
                ThreadObject.class
        ).getBody();

    }

    public VectorStoreObject createVectorStore(){
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "Quiz Resources Storage");

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        return restTemplate.exchange(
                "https://api.openai.com/v1/vector_stores",
                HttpMethod.POST,
                requestEntity,
                VectorStoreObject.class
        ).getBody();

    }

}
