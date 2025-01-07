package com.newbini.quizard.api;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;


public class OpenAIBasicHeaderConst {

    public static HttpHeaders basicHeader(String key) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(key);
        headers.add("OpenAI-Beta", "assistants=v2");

        return headers;
    }
}
