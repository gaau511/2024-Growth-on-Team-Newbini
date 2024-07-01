package com.newbini.newbeinquiz.api;

import com.newbini.newbeinquiz.dto.response.ResultObject;
import com.newbini.newbeinquiz.dto.response.RunObject;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ExecuteManager {
    private final String openAiApiKey;
    private final RestTemplate restTemplate;
    private HttpHeaders headers;

    @PostConstruct
    public void initialize() {
        this.headers = OpenAIBasicHeaderConst.basicHeader(openAiApiKey);
    }

    /**
     * Run assistant
     */
    public String run(String thread_id, String assistant_id) throws InterruptedException {
        String url = "https://api.openai.com/v1/threads/" +thread_id+"/runs";

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("assistant_id", assistant_id);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        RunObject runObject = restTemplate.postForEntity(url, requestEntity, RunObject.class).getBody();

        String run_id = runObject.getId();
        String answer = getAnswer(thread_id, run_id);
        System.out.println("answer = " + answer);

        return answer;
    }

    /**
     *When stream == false, we can't use the SSE method,
     * so we retrieve once every second to check if the answer is complete
     */
    private String getAnswer(String thread_id, String run_id) throws InterruptedException {
        while(true) {
            Thread.sleep(1000);
            if (retrieveRun(thread_id, run_id) == true) {
                ResultObject result = sendFinalRequest(thread_id);
                String answer = result.getData().get(0).getContent().get(0).getText().getValue();
                return answer;
            }
        }
    }

    /**
     * After the answer generation is confirmed, call it.
     */
    private ResultObject sendFinalRequest(String thread_id) {
        String url = "https://api.openai.com/v1/threads/"+thread_id+"/messages";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(openAiApiKey);
        headers.add("OpenAI-Beta", "assistants=v2");

        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<ResultObject> result = restTemplate.exchange(url, HttpMethod.GET, requestEntity, ResultObject.class);
        ResultObject body = result.getBody();
        return body;

    }



    public Boolean retrieveRun(String thread_id,String run_id) {
        String url = "https://api.openai.com/v1/threads/"+ thread_id +"/runs/" + run_id;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(openAiApiKey);
        headers.add("OpenAI-Beta", "assistants=v2");

        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<RunObject> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, RunObject.class);
        RunObject runObject = response.getBody();

        if (runObject.getStatus().equals("completed")) {
            return true;
        }

        return false;

    }


}
