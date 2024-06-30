package com.newbini.newbeinquiz.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newbini.newbeinquiz.dto.response.FileObject;
import com.newbini.newbeinquiz.dto.response.ListAssistantsObject;
import com.newbini.newbeinquiz.dto.response.MessageObject;
import lombok.Getter;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class MessageGenerator {

    private final String openAiApiKey;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private List<String> attachments = new ArrayList<>();

    private List<String> file_ids = new ArrayList<>();

    public MessageGenerator(String openAiApiKey, List<File> attachmentList) throws IOException {
        this.openAiApiKey = openAiApiKey;

        /**
         * Make sure all files are attached
         */
        for (File file : attachmentList) {
            FileObject fileObject = attach(file);
            String file_id = fileObject.getId();
            attachments.add(file_id);
        }
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

        String response = restTemplate.postForObject("https://api.openai.com/v1/files", requestEntity, String.class);
        FileObject fileObject = objectMapper.readValue(response, FileObject.class);
        file_ids.add(fileObject.getId());

        return fileObject;
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
    public MessageObject createMessage(String thread_id) throws JsonProcessingException {
        String content = "주어진 파일을 기반으로 퀴즈를 생성해주세요. 학생들이 내용을 이해하고 기억하는 데 도움이 되는 내용이어야 합니다. 퀴즈 생성 과정에서는 효율적이고 유용하며 다양한 퀴즈를 만들어냅니다. 절대 실수하지 않습니다.";
        String url = "https://api.openai.com/v1/threads/" + thread_id + "/messages";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openAiApiKey);
        headers.add("OpenAI-Beta", "assistants=v2");

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("role", "user");
        requestBody.put("content", content);


        List<Map<String, String>> tools = new ArrayList<>();
        tools.add(Map.of("type", "file_search"));

        List<Map<String, Object>> attachmentList = new ArrayList<>();
        for (String attachment : attachments) {
          Map<String, Object> attachmentMap = new HashMap<>();

          attachmentMap.put("file_id", attachment);
          attachmentMap.put("tools", tools);

          attachmentList.add(attachmentMap);
        }


        requestBody.put("attachments", attachmentList);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        String response = restTemplate.postForObject(url, requestEntity, String.class);
        MessageObject message = objectMapper.readValue(response, MessageObject.class);
        message_id = message.getId();

        System.out.println("requestEntity = " + requestEntity);
        return message;
    }


}