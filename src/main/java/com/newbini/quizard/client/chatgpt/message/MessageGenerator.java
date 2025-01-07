package com.newbini.quizard.client.chatgpt.message;

import com.newbini.quizard.client.chatgpt.common.OpenAIBasicHeaderConst;
import com.newbini.quizard.client.chatgpt.common.PromptConst;
import com.newbini.quizard.dto.response.FileObject;
import com.newbini.quizard.dto.response.MessageObject;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Component
@RequiredArgsConstructor
public class MessageGenerator {

    private final String openAiApiKey;
    private final RestTemplate restTemplate;
    private HttpHeaders headers;

    @PostConstruct
    public void initialize() {
        this.headers = OpenAIBasicHeaderConst.basicHeader(openAiApiKey);
    }

    public List<String> attachFiles(List<File> fileList) throws IOException {
        List<String> fileIdList = new ArrayList<>();
        for (File file : fileList) {
            fileIdList.add(attach(file).getId());
        }
        return fileIdList;
    }

    /**
     * File attach
     */
    public FileObject attach(File file) {

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(openAiApiKey);
        headers.add("OpenAI-Beta", "assistants=v2");
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("file", new FileSystemResource(file));
        requestBody.add("purpose", "assistants");

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        return restTemplate.exchange(
                "https://api.openai.com/v1/files",
                HttpMethod.POST,  // HTTP 메소드
                requestEntity,
                FileObject.class
        ).getBody();
    }

    /**
     * Assitant Api 에게 보내는 메시지 생성
     *
     * @param thread_id
     * @return message Object
     */
    public MessageObject createMessage(String thread_id, List<String> fileIdList) {
        String url = "https://api.openai.com/v1/threads/" + thread_id + "/messages";

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("role", "user");
        requestBody.put("content", PromptConst.createMessagePrompt());


        List<Map<String, String>> tools = new ArrayList<>();
        tools.add(Map.of("type", "file_search"));

        List<Map<String, Object>> attachmentList = new ArrayList<>();
        for (String id : fileIdList) {
          Map<String, Object> attachmentMap = new HashMap<>();

          attachmentMap.put("file_id", id);
          attachmentMap.put("tools", tools);

          attachmentList.add(attachmentMap);
        }

        requestBody.put("attachments", attachmentList);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        return restTemplate.exchange(
                url,
                HttpMethod.POST,  // HTTP 메소드
                requestEntity,
                MessageObject.class
        ).getBody();
    }


}