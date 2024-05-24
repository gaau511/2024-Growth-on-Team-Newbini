package com.newbini.newbeinquiz.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newbini.newbeinquiz.web.response.FileObject;
import com.newbini.newbeinquiz.web.response.MessageObject;
import jakarta.annotation.PostConstruct;
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
    private List<String> file_ids = new ArrayList<>();
    private String message_id = "";


    public MessageGenerator(String openAiApiKey) {
        this.openAiApiKey = openAiApiKey;
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
        String url = "https://api.openai.com/v1/threads/" + thread_id + "/messages";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openAiApiKey);
        headers.add("OpenAI-Beta", "assistants=v2");

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("role", "user");
        requestBody.put("content", "Based on these files, make quizzes for me.");

        Map<String, Object> attachment = new HashMap<>();
        if (!file_ids.isEmpty()) {
            for (String file_id : file_ids) {
                attachment.put("file_id", file_id);
            }
        }

        Map<String, Object> tool = new HashMap<>();
        tool.put("type", "file_search");

        attachment.put("tools", Collections.singletonList(tool));

        requestBody.put("attachments", Collections.singletonList(attachment));

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        String response = restTemplate.postForObject(url, requestEntity, String.class);
        MessageObject message = objectMapper.readValue(response, MessageObject.class);
        message_id = message.getId();

        return message;
    }


}
