package com.newbini.newbeinquiz.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newbini.newbeinquiz.web.response.FileObject;
import com.newbini.newbeinquiz.web.response.MessageObject;
import jakarta.annotation.PostConstruct;
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
    private final List<File> attachmentList;
    private List<String> file_ids = new ArrayList<>();

    @Getter
    private String message_id = "";
    private Map<String, Object> attachment = new HashMap<>();


    public MessageGenerator(String openAiApiKey, List<File> attachmentList) throws IOException {
        this.openAiApiKey = openAiApiKey;
        this.attachmentList = attachmentList;

        for (File file : attachmentList) {
            FileObject fileObject = attachForTest(file);
            String file_id = fileObject.getId();
            attachment.put("file_id", file_id);
        }
    }

    public void attachFiles(List<MultipartFile> files) throws IOException {
        if (!files.isEmpty()) {
            for (MultipartFile file : files) {
                attach(file);
            }
        }
    }

    /**
        Method For Test
     */
    public FileObject attachForTest(File file) throws IOException {

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

    public FileObject attach(MultipartFile file) throws IOException {
        File new_file = MultipartFileConverter(file);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(openAiApiKey);
        headers.add("OpenAI-Beta", "assistants=v2");
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);


        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("file", new FileSystemResource(new_file));
        requestBody.add("purpose", "assistants");

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        String response = restTemplate.postForObject("https://api.openai.com/v1/files", requestEntity, String.class);
        FileObject fileObject = objectMapper.readValue(response, FileObject.class);
        file_ids.add(fileObject.getId());

        return fileObject;
    }

    private File MultipartFileConverter(MultipartFile mfile) throws IOException {
        File file = new File(mfile.getOriginalFilename());
        mfile.transferTo(file);
        return file;
    }

    public MessageObject createMessage(String thread_id) throws JsonProcessingException {
        String content = "주어진 강의 자료를 기반으로 퀴즈를 생성해주세요. 학생들이 강의 내용을 이해하고 기억하는 데 도움이 되는 내용이어야 합니다. 퀴즈 생성 과정에서는 효율적이고 유용하며 다양한 퀴즈를 만들어냅니다. 절대 실수하지 않습니다.";
        String url = "https://api.openai.com/v1/threads/" + thread_id + "/messages";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openAiApiKey);
        headers.add("OpenAI-Beta", "assistants=v2");

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("role", "user");
        requestBody.put("content", content);


        Map<String, Object> tool = new HashMap<>();
        tool.put("type", "file_search");

        attachment.put("tools", Collections.singletonList(tool));
        requestBody.put("attachments", Collections.singletonList(attachment));

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        System.out.println("requestEntity = " + requestEntity);
        String response = restTemplate.postForObject(url, requestEntity, String.class);
        MessageObject message = objectMapper.readValue(response, MessageObject.class);
        message_id = message.getId();

        return message;
    }


}
