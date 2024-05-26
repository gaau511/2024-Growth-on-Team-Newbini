package com.newbini.newbeinquiz.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.newbini.newbeinquiz.chat.AssistantGenerator;
import com.newbini.newbeinquiz.chat.ExecuteManager;
import com.newbini.newbeinquiz.chat.MessageGenerator;
import com.newbini.newbeinquiz.web.request.MultiPartFilesForm;
import com.newbini.newbeinquiz.web.response.AssistantObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RestController
public class UploadController {

    @Value("${openai.api.key}")
    private String apiKey;

    @PostMapping("/upload")
    @ResponseBody
    public String handleFileUpload(@RequestParam("attach_file") List<MultipartFile> files) throws IOException, InterruptedException {
        log.info("files ={}", files);
        List<File> fileListForAttach = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file != null && !file.isEmpty()) {
                String fileName = file.getOriginalFilename();
                File dest = new File("C:\\Users\\82109\\Desktop\\Newbini\\src\\main\\resources\\files\\" + fileName);
                try {
                    file.transferTo(dest);
                    fileListForAttach.add(dest);
                } catch (IOException e) {
                    return "error";
                }
            }
        }

        AssistantGenerator assistant = new AssistantGenerator(apiKey);
        System.out.println("assistant = " + assistant);

        assistant.createAssistant();
        System.out.println("assistant = " + assistant);

        String assistant_id = assistant.getAssistant_id();
        System.out.println("assistant_id = " + assistant_id);

        assistant.createThread();
        System.out.println("assistant_id = " + assistant_id);

        String thread_id = assistant.getThread_id();
        System.out.println("thread_id = " + thread_id);

        MessageGenerator message = new MessageGenerator(apiKey, fileListForAttach);
        System.out.println("message = " + message);

        message.createMessage(thread_id);
        System.out.println("message = " + message);

        String message_id = message.getMessage_id();
        System.out.println("message_id = " + message_id);

        ExecuteManager execute = new ExecuteManager(apiKey);
        System.out.println("execute = " + execute);
        String answer = execute.run(thread_id, assistant_id);
        System.out.println("answer = " + answer);

        return answer;
    }
}
