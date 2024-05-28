package com.newbini.newbeinquiz.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newbini.newbeinquiz.chat.AssistantGenerator;
import com.newbini.newbeinquiz.chat.ExecuteManager;
import com.newbini.newbeinquiz.chat.MessageGenerator;
import com.newbini.newbeinquiz.web.request.Quiz;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping
public class UploadController {

    ObjectMapper objectMapper = new ObjectMapper();

    @Value("${openai.api.key}")
    private String apiKey;

    private String type;
    private String difficulty;

    @GetMapping("/upload")
    public String uploadForm( @RequestParam("type") String type,
                              @RequestParam("difficulty") String difficulty) {

        this.type = type;
        this.difficulty = difficulty;

        System.out.println("type = " + type);
        System.out.println("difficulty = " + difficulty);
        return "file-upload";
    }

    @PostMapping("/result")
    public String handleFileUpload(@RequestParam("attach_file") List<MultipartFile> files, RedirectAttributes redirectAttributes, Model model) throws IOException, InterruptedException, ParseException {
        log.info("files ={}", files);
        List<File> fileListForAttach = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file != null && !file.isEmpty()) {
                String fileName = file.getOriginalFilename();
                File dest = new File("C:\\Users\\82109\\Desktop\\Newbini\\src\\main\\resources\\files\\" + fileName);
                file.transferTo(dest);
                fileListForAttach.add(dest);
            }
        }


        AssistantGenerator assistant = new AssistantGenerator(apiKey);
        System.out.println("assistant = " + assistant);

        assistant.createAssistant(type,difficulty);
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

        Quiz quiz = objectMapper.readValue(answer, Quiz.class);
        model.addAttribute("quiz",quiz);
        return "result";
    }
}
