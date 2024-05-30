package com.newbini.newbeinquiz.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;


public class AudioHandler {

    private String uploadDir;
    private String openAiApiKey;
    RestTemplate restTemplate = new RestTemplate();

    public AudioHandler(String uploadDir, String key) {
        this.uploadDir = uploadDir;
        this.openAiApiKey = key;
    }

    File audioToEnglish(File file) throws IOException {
        String url = "https://api.openai.com/v1/audio/transcriptions";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + openAiApiKey);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", new FileSystemResource(file));
        builder.part("model", "whisper-1");
        builder.part("response_format", "text");

        HttpEntity<?> entity = new HttpEntity<>(builder.build(), headers);
        System.out.println("entity = " + entity);
        String body = restTemplate.exchange(url, HttpMethod.POST, entity, String.class).getBody();
        return stringToTxtFile(file.getName(), body);
    }

    private File stringToTxtFile(String name, String str) throws IOException {
        String filePath = uploadDir + "/" + name + "_transcription.txt";

        Path path = Files.write(Paths.get(filePath), str.getBytes(), StandardOpenOption.CREATE);
        return path.toFile();
    }

}
