package com.newbini.newbeinquiz.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newbini.newbeinquiz.web.response.AssistantCreateResponse;
import com.newbini.newbeinquiz.web.response.FileAttachResponse;
import com.newbini.newbeinquiz.web.response.FileSummaryResponse;
import com.newbini.newbeinquiz.web.response.ThreadResponse;
import jakarta.annotation.PostConstruct;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Component
public class FileHandler {

    private final String openAiApiKey;
    private String thread_id = "";
    private String file_id = "";
    private String assistant_id = "";
    private String message_id = "";

    public FileHandler(String openAiApiKey) {
        this.openAiApiKey = openAiApiKey;
    }

    @PostConstruct
    public void create() throws JsonProcessingException {
        System.out.println("FileHandler.create");
        assistant_id = createAssistant();
    }

    private String createAssistant() throws JsonProcessingException {
        System.out.println("FileHandler.createAssistant");
        HttpResponse<String> response = Unirest.post("https://api.openai.com/v1/assistants")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + openAiApiKey)
                .header("OpenAI-Beta", "assistants=v2")
                .body("{\n" +
                        "    \"instructions\": \"You are a personal math tutor. When asked a question, write and run Python code to answer the question.\",\n" +
                        "    \"name\": \"Math Tutor\",\n" +
                        "    \"tools\": [{\"type\": \"file_search\"}],\n" +
                        "    \"model\": \"gpt-4-turbo\"\n" +
                        "  }")
                .asString();

        String body = response.getBody();
        System.out.println("jsonResponse = " + body);

        ObjectMapper objectMapper = new ObjectMapper();
        AssistantCreateResponse acResponse = objectMapper.readValue(body, AssistantCreateResponse.class);

        System.out.println("acResponse.getId() = " + acResponse.getId());
        return acResponse.getId();
    }


    public String attach(MultipartFile file) throws IOException {
        File new_file = MultipartFileConverter(file);

        HttpResponse<String> response = Unirest.post("https://api.openai.com/v1/files")
                .header("Authorization", "Bearer " + openAiApiKey)
                //TODO 실제 파일로 대체 해야함
                .field("file", new_file)
                .field("purpose", "assistants")
                .asString();

        return response.getBody();
    }

    public String attach(File file) throws IOException {
        System.out.println("FileHandler.attach");
        HttpResponse<String> response = Unirest.post("https://api.openai.com/v1/files")
                .header("Authorization", "Bearer " + openAiApiKey)
                //TODO 실제 파일로 대체 해야함
                .field("file", file)
                .field("purpose", "assistants")
                .asString();

        //Assistant를 위한 thread 생성
        thread_id = createThread();
        System.out.println("thread_id = " + thread_id);

        String jsonResponse = response.getBody();

        // JSON 문자열을 객체로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        FileAttachResponse fileAttachResponse = objectMapper.readValue(jsonResponse, FileAttachResponse.class);

        file_id = fileAttachResponse.getId();
        System.out.println("file_id = " + file_id);
        return fileAttachResponse.getId();
    }

    private String createThread() throws JsonProcessingException {
        System.out.println("FileHandler.createThread");
        HttpResponse<String> response = Unirest.post("https://api.openai.com/v1/threads")
                .header("Authorization", "Bearer " + openAiApiKey)
                .header("OpenAI-Beta", "assistants=v2")
                .asString();

        String jsonResponse = response.getBody();

        // JSON 문자열을 객체로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        ThreadResponse threadResponse = objectMapper.readValue(jsonResponse, ThreadResponse.class);

        return threadResponse.getId();
    }


    private File MultipartFileConverter(MultipartFile mfile) throws IOException {
        File file = new File(mfile.getOriginalFilename());
        mfile.transferTo(file);
        return file;
    }

    public String sendMessage() throws JsonProcessingException {
        System.out.println("FileHandler.summarize");
        HttpResponse<String> response = Unirest.post("https://api.openai.com/v1/threads/"
                +thread_id+"/messages")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + openAiApiKey)
                .header("OpenAI-Beta", "assistants=v2")
                .body("{\n" +
                        "\t\"role\": \"user\",\n" +
                        "\t\"content\": \"Please summarize this file.\",\n" +
                        "\t\"attachments\": [\n" +
                        "\t\t{\n" +
                        "\t\t\t\"file_id\": \""+file_id+"\",\n" +
                        "\t\t\t\"tools\": [\n" +
                        "\t\t\t\t{ \n" +
                        "\t\t\t\t\t\"type\": \"file_search\"\n" +
                        "\t\t\t\t}\n" +
                        "\t\t\t]\n" +
                        "\t\t}\n" +
                        "\t]\n" +
                        "}")
                .asString();


        String jsonResponse = response.getBody();

        // JSON 문자열을 객체로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        FileSummaryResponse fileSummaryResponse = objectMapper.readValue(jsonResponse, FileSummaryResponse.class);

        System.out.println(jsonResponse);
        message_id = fileSummaryResponse.getId();
        return fileSummaryResponse.getId();
    }

    public String run() {
        System.out.println("FileHandler.run");
        HttpResponse<String> response = Unirest.post("https://api.openai.com/v1/threads/"
                        +thread_id+"/runs")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + openAiApiKey)
                .header("OpenAI-Beta", "assistants=v2")
                .body("{\n" +
                        "    \"assistant_id\": \""+assistant_id+"\",\n" +
                        "    \"stream\": true\n" +
                        "  }")
                .asString();

        String body = response.getBody();
        System.out.println(body);
        return "";
    }
}
