package com.newbini.newbeinquiz.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newbini.newbeinquiz.Repository.QuizRepository;
import com.newbini.newbeinquiz.api.AssistantDeleteManager;
import com.newbini.newbeinquiz.api.AssistantGenerator;
import com.newbini.newbeinquiz.api.ExecuteManager;
import com.newbini.newbeinquiz.api.MessageGenerator;
import com.newbini.newbeinquiz.domain.Quiz;
import com.newbini.newbeinquiz.dto.request.QuizForm;
import com.newbini.newbeinquiz.dto.response.*;
import com.newbini.newbeinquiz.member.Member;
import com.newbini.newbeinquiz.member.MemberRepository;
import com.newbini.newbeinquiz.member.TemporalQuizRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping
public class UploadController {

    private final ObjectMapper objectMapper;
    private final AssistantGenerator assistant;
    private final MessageGenerator message;
    private final AudioHandler audioHandler;
    private final AssistantDeleteManager assistantDeleteManager;
    private final ExecuteManager execute;
    private final QuizRepository quizRepository;
    private final MemberRepository memberRepository;

    private final TemporalQuizRepository temporalQuizRepository;

    @GetMapping("/upload")
    public String uploadForm() {
        return "file-upload";
    }


    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("attach_file") List<MultipartFile> mfiles,
                                   @RequestParam(value = "type", defaultValue = "객관식,주관식,O/X") String type,
                                   @RequestParam(value = "difficulty", defaultValue = "보통") String difficulty,
                                   @SessionAttribute(name = "loginMember", required = false) Member loginMember,
                                   RedirectAttributes redirectAttributes) throws IOException, InterruptedException, ParseException {

        // Convert MultipartFiles to Files
        List<File> fileListForAttach = MultipartToFile(mfiles);
        String answer = RunExecuteSequence(fileListForAttach, type, difficulty);

        QuizForm createdQuiz = objectMapper.readValue(answer, QuizForm.class);

        // 회원정보 꺼내기
        // Quiz DB에 저장
        if (loginMember != null) {
            String quizHash= UUID.randomUUID().toString();
            Long memberId = loginMember.getId();
            for (QuizForm.Question question : createdQuiz.getQuestions()) {
                Quiz quiz = new Quiz(memberId, quizHash, question.getIndex(), question.getQuestion(), question.getAnswer());
                quizRepository.save(quiz);
            }
            //member의 latest update
            loginMember.setLatest(quizHash);
            memberRepository.save(loginMember);
        }

        redirectAttributes.addFlashAttribute("quiz", createdQuiz);

        return "redirect:/result";

    }

    /**
     *
     * @param fileListForAttach
     * @param type
     * @param difficulty
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    private String RunExecuteSequence(List<File> fileListForAttach, String type, String difficulty) throws IOException, InterruptedException {

        // 1. create assistant
        AssistantObject createdAssistant = assistant.createAssistant(type, difficulty);
        log.debug("assistant : {}", createdAssistant);
        log.info("createAssistant success");
        String assistant_id = createdAssistant.getId();

        // 2. create thread
        ThreadObject thread = assistant.createThread();
        log.debug("thread : {}", thread);
        log.info("createThread success");
        String thread_id = thread.getId();

        // 3. Attach files to Message
        List<String> fileIdList = message.attachFiles(fileListForAttach);

        // 4. create message
        MessageObject createdMessage = message.createMessage(thread_id, fileIdList);
        log.debug("createdMessage : {}", createdMessage);
        log.info("createMessage success");
        String message_id = createdMessage.getId();

        log.info("running start");
        //4. run assistant
        String answer = execute.run(thread_id, assistant_id);
        log.info("run success");

        //5. delete assistant
        DeleteAssistantObject delete = assistantDeleteManager.deleteAssistant(assistant_id);
        log.info("assistant deleted");

        // 6. return answer
        return answer;
    }

    /**
     * Make FileList for attach and save files in 'resources/files/'
     *
     * @param multipartFiles
     * @return files
     * @throws IOException
     */
    private List<File> MultipartToFile(List<MultipartFile> multipartFiles) throws IOException {
        List<File> files = new ArrayList<>();
        for (MultipartFile mfile : multipartFiles) {
            if (mfile != null && !mfile.isEmpty()) {
                File tempFile = Files.createTempFile("temp_",mfile.getOriginalFilename()).toFile();
                mfile.transferTo(tempFile);

                // if type is audio, convert to TxtFile
                if (mfile.getContentType().startsWith("audio")) {
                    File textFile = audioHandler.audioToText(tempFile);
                    textFile.deleteOnExit();
                    files.add(textFile);
                } else {
                    files.add(tempFile);
                }
                tempFile.deleteOnExit();
            }
        }

        return files;
    }
}