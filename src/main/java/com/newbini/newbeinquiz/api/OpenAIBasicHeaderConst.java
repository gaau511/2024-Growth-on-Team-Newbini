package com.newbini.newbeinquiz.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;


public class OpenAIBasicHeaderConst {

    public static HttpHeaders basicHeader(String key) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(key);
        headers.add("OpenAI-Beta", "assistants=v2");

        return headers;
    }
}
