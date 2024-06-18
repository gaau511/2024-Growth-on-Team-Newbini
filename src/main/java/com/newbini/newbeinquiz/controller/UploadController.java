package com.newbini.newbeinquiz.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newbini.newbeinquiz.api.AssistantGenerator;
import com.newbini.newbeinquiz.api.ExecuteManager;
import com.newbini.newbeinquiz.api.MessageGenerator;
import com.newbini.newbeinquiz.dto.request.QuizForm;
import com.newbini.newbeinquiz.dto.response.AssistantObject;
import com.newbini.newbeinquiz.dto.response.MessageObject;
import com.newbini.newbeinquiz.dto.response.ThreadObject;
import com.newbini.newbeinquiz.member.Member;
import com.newbini.newbeinquiz.member.TemporalQuizRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
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

    @Value("${file.dir}")
    private String uploadDir;

    @Value("${openai.api.key}")
    private String key;

    private final TemporalQuizRepository temporalQuizRepository;


    private String type;
    private String difficulty;

    /**
     * When an AudioFile is input,
     * it must be transcribed through an audio handler.
     */
    private AudioHandler audioHandler;

    @GetMapping("/upload")
    public String uploadForm( @RequestParam(value = "type", defaultValue = "객관식,주관식,O/X") String type,
                              @RequestParam(value = "difficulty", defaultValue = "보통") String difficulty) {

        this.type = type;
        this.difficulty = difficulty;

        return "file-upload";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("attach_file") List<MultipartFile> files,
                                   @SessionAttribute(name = "loginMember", required = false) Member loginMember,
                                   RedirectAttributes redirectAttributes) throws IOException, InterruptedException, ParseException {
        audioHandler = new AudioHandler(uploadDir, key);

        /**
         * Make file List to Attach
         */
        List<File> fileListForAttach = new ArrayList<>();
        makeFileAttachList(files, fileListForAttach);

        String answer = RunExecuteSequence(fileListForAttach);

        QuizForm quiz = objectMapper.readValue(answer, QuizForm.class);

        // 회원정보 꺼내기
        if (loginMember != null) {
            temporalQuizRepository.storeQuiz(loginMember.getUuid(), quiz);
        }

        redirectAttributes.addFlashAttribute("quiz", quiz);

        return "redirect:/result";

    }

    /**
     * @param fileListForAttach
     * @return Quiz creation result in jsonString
     */
    private String RunExecuteSequence(List<File> fileListForAttach) throws ParseException, IOException, InterruptedException {

        // 1. create assitant
        AssistantGenerator assistant = new AssistantGenerator(key);

        AssistantObject createdAssistant = assistant.createAssistant(type, difficulty);
        log.debug("assistant : {}", createdAssistant);
        log.info("createAssistant success");

        String assistant_id = createdAssistant.getId();

        // 2. create thread
        ThreadObject thread = assistant.createThread();
        log.debug("thread : {}", thread);
        log.info("createThread success");

        String thread_id = thread.getId();

        MessageGenerator message = new MessageGenerator(key, fileListForAttach);

        // 3. Attach files, create message
        MessageObject createdMessage = message.createMessage(thread_id);
        log.debug("createdMessage : {}", createdMessage);
        log.info("createMessage success");

        String message_id = createdMessage.getId();

        ExecuteManager execute = new ExecuteManager(key);
        log.info("running start");

        //4. run assistant
        String answer = execute.run(thread_id, assistant_id);
        log.info("run success");

        // 5. get answer
        return answer;
    }

    /**
     * Make FileList for attach and save files in 'resources/files/'
     *
     * @param files
     * @param fileListForAttach
     * @throws IOException
     */
    private void makeFileAttachList(List<MultipartFile> files, List<File> fileListForAttach) throws IOException {
        for (MultipartFile file : files) {
            if (file != null && !file.isEmpty()) {
                String fileName = file.getOriginalFilename();
                File dest = new File(uploadDir + fileName);
                file.transferTo(dest);

                // if type is audio, convert to TxtFile
                if (file.getContentType().startsWith("audio")) {
                    File textFile = audioHandler.audioToText(dest);
                    fileListForAttach.add(textFile);
                } else {
                    fileListForAttach.add(dest);

                }
            }
        }
    }
}