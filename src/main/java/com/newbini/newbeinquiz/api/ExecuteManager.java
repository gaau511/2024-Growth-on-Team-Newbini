package com.newbini.newbeinquiz.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newbini.newbeinquiz.web.response.ResultObject;
import com.newbini.newbeinquiz.web.response.RunObject;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class ExecuteManager {
    private final String openAiApiKey;
    private final RestTemplate restTemplate = new RestTemplate();
    private String run_id = "";
    private String thread_id = "";

    public ExecuteManager(String openAiApiKey) {
        this.openAiApiKey = openAiApiKey;
    }

    public String run(String thread_id, String assistant_id) throws InterruptedException {
        this.thread_id = thread_id;

        String url = "https://api.openai.com/v1/threads/" +thread_id+"/runs";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openAiApiKey);
        headers.add("OpenAI-Beta", "assistants=v2");

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("assistant_id", assistant_id);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<RunObject> response = restTemplate.postForEntity(url, requestEntity, RunObject.class);
        RunObject runObject = response.getBody();

        run_id = runObject.getId();
        String answer = getAnswer();
        System.out.println("answer = " + answer);

        return answer;
    }

    private String getAnswer() throws InterruptedException {
        while(true) {
            Thread.sleep(1000);
            if (retrieveRun() == true) {
                ResultObject result = sendFinalRequest();
                String answer = result.getData().get(0).getContent().get(0).getText().getValue();
                return answer;
            }
        }
    }

    private ResultObject sendFinalRequest() {
        String url = "https://api.openai.com/v1/threads/"+thread_id+"/messages";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(openAiApiKey);
        headers.add("OpenAI-Beta", "assistants=v2");

        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<ResultObject> result = restTemplate.exchange(url, HttpMethod.GET, requestEntity, ResultObject.class);
        ResultObject body = result.getBody();
        return body;

    }



    public Boolean retrieveRun() {
        String url = "https://api.openai.com/v1/threads/"+ this.thread_id +"/runs/" + run_id;

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
