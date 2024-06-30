package com.newbini.newbeinquiz.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newbini.newbeinquiz.dto.response.AssistantObject;
import com.newbini.newbeinquiz.dto.response.FileObject;
import com.newbini.newbeinquiz.dto.response.ListAssistantsObject;
import com.newbini.newbeinquiz.dto.response.MessageObject;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Component
@RequiredArgsConstructor
public class MessageGenerator {

    private final String openAiApiKey;
    private final RestTemplate restTemplate;

    private HttpHeaders headers;
    private List<String> attachments = new ArrayList<>();

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

    @Getter
    private String message_id = "";

    /**
     * File attach
     */
    public FileObject attach(File file) throws IOException {

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
     * multipart -> file converter
     */
    private File MultipartFileConverter(MultipartFile mfile) throws IOException {
        File file = new File(mfile.getOriginalFilename());
        mfile.transferTo(file);
        return file;
    }

    /**
     * Assitant Api 에게 보내는 메시지 생성
     *
     * @param thread_id
     * @return message Object
     */
    public MessageObject createMessage(String thread_id, List<String> fileIdList) throws JsonProcessingException {
        String url = "https://api.openai.com/v1/threads/" + thread_id + "/messages";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openAiApiKey);
        headers.add("OpenAI-Beta", "assistants=v2");

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