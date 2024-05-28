package com.newbini.newbeinquiz.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newbini.newbeinquiz.api.AssistantGenerator;
import com.newbini.newbeinquiz.api.ExecuteManager;
import com.newbini.newbeinquiz.api.MessageGenerator;
import com.newbini.newbeinquiz.web.request.Quiz;
import com.newbini.newbeinquiz.web.response.AssistantObject;
import com.newbini.newbeinquiz.web.response.MessageObject;
import com.newbini.newbeinquiz.web.response.ThreadObject;
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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping
public class UploadController {

    ObjectMapper objectMapper = new ObjectMapper();

    @Value("${file.dir}")
    private String uploadDir;

    @Value("${openai.api.key}")
    private String key;

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

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("attach_file") List<MultipartFile> files, RedirectAttributes redirectAttributes) throws IOException, InterruptedException, ParseException {
        log.info("files ={}", files);
        List<File> fileListForAttach = new ArrayList<>();
        makeFileAttachList(files, fileListForAttach);

        String answer = RunExecuteSequence(fileListForAttach);

        Quiz quiz = objectMapper.readValue(answer, Quiz.class);
        redirectAttributes.addFlashAttribute("quiz", quiz);
        return "redirect:/result";
    }

    private String RunExecuteSequence(List<File> fileListForAttach) throws ParseException, IOException, InterruptedException {

        AssistantGenerator assistant = new AssistantGenerator(key);

        AssistantObject createdAssistant = assistant.createAssistant(type, difficulty);
        log.debug("assistant : {}", createdAssistant);
        log.info("createAssistant success");

        String assistant_id = createdAssistant.getId();

        ThreadObject thread = assistant.createThread();
        log.debug("thread : {}", thread);
        log.info("createThread success");

        String thread_id = thread.getId();

        MessageGenerator message = new MessageGenerator(key, fileListForAttach);

        MessageObject createdMessage = message.createMessage(thread_id);
        log.debug("createdMessage : {}", createdMessage);
        log.info("createMessage success");

        String message_id = createdMessage.getId();

        ExecuteManager execute = new ExecuteManager(key);
        String answer = execute.run(thread_id, assistant_id);
        log.info("run success");

        return answer;
    }

    private void makeFileAttachList(List<MultipartFile> files, List<File> fileListForAttach) throws IOException {
        Path uploadPath = Paths.get(uploadDir);

        for (MultipartFile file : files) {
            if (file != null && !file.isEmpty()) {
                String fileName = file.getOriginalFilename();
                File dest = new File(uploadPath + "\\" + fileName);
                file.transferTo(dest);
                fileListForAttach.add(dest);
            }
        }
    }
}
