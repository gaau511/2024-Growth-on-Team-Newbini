package com.newbini.newbeinquiz.controller;

import com.fasterxml.jackson.core.JsonParseException;
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
    private AudioHandler audioHandler;

    @GetMapping("/upload")
    public String uploadForm( @RequestParam(value = "type", defaultValue = "객관식,주관식,O/X") String type,
                              @RequestParam(value = "difficulty", defaultValue = "보통") String difficulty) {

        this.type = type;
        this.difficulty = difficulty;

        return "file-upload";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("attach_file") List<MultipartFile> files, RedirectAttributes redirectAttributes) throws IOException, InterruptedException, ParseException {
        audioHandler = new AudioHandler(uploadDir, key);

        List<File> fileListForAttach = new ArrayList<>();
        makeFileAttachList(files, fileListForAttach);

        String answer = RunExecuteSequence(fileListForAttach);

        try {
            Quiz quiz = objectMapper.readValue(answer, Quiz.class);
            redirectAttributes.addFlashAttribute("quiz", quiz);
        }
        catch (JsonParseException e){
            redirectAttributes.addFlashAttribute("jsonError", answer);
        }

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
        log.info("running start");

        String answer = execute.run(thread_id, assistant_id);
        log.info("run success");

        return answer;
    }

    private void makeFileAttachList(List<MultipartFile> files, List<File> fileListForAttach) throws IOException {

        for (MultipartFile file : files) {
            if (file != null && !file.isEmpty()) {
                String fileName = file.getOriginalFilename();
                File dest = new File(uploadDir + fileName);
                file.transferTo(dest);

                if (file.getContentType().startsWith("audio")) {
                    File textFile = audioHandler.audioToEnglish(dest);
                    fileListForAttach.add(textFile);
                } else {
                    fileListForAttach.add(dest);

                }
            }
        }
    }
}
