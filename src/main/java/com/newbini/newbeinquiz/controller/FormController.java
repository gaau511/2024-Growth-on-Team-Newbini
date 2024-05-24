package com.newbini.newbeinquiz.controller;

import com.newbini.newbeinquiz.chat.ChatHandler;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/upload")
@Slf4j
@RequiredArgsConstructor
public class FormController {

    @GetMapping
    public String uploadForm() {
        return "upload-form";
    }

    @PostMapping
    public String upload(@RequestParam(value = "audioFile")MultipartFile audioFile,
                             @RequestParam(value = "pdfFile")MultipartFile pdfFile,
                             @RequestParam(value = "textFile")MultipartFile textFile,
                             @RequestParam(value = "textInput")String textInput,
                             HttpServletRequest request) throws IOException {

        ChatHandler chatHandler = new ChatHandler(audioFile, pdfFile,textFile,textInput);
        String audioTranscription = chatHandler.transcript();
//        String pdfSummary = chatHandler.summarize();

        return "";
    }
}
