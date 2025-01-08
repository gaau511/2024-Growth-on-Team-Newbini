package com.newbini.quizard.client.chatgpt.assistant;

import com.newbini.quizard.client.chatgpt.common.PromptConst;
import com.newbini.quizard.dto.response.AssistantObject;
import com.newbini.quizard.dto.response.ThreadObject;
import com.newbini.quizard.dto.response.VectorStoreObject;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class AssistantGenerator {
    private final String openAiApiKey;
    private final RestTemplate restTemplate;
    private HttpHeaders headers;

    @PostConstruct
    public void initialize() {
        this.headers = OpenAIBasicHeaderConst.basicHeader(openAiApiKey);
    }


    public AssistantObject createAssistant(String type, String difficulty) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("instructions", PromptConst.createInstruction(type, difficulty));
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
