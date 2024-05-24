package com.newbini.newbeinquiz.chat;

import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class ChatHandler{

    private final MultipartFile audio;
    private final MultipartFile courseMaterial;
    private final MultipartFile handWriting;
    private final String text;

    @Autowired OpenAiAudioApi openAiAudioApi;
    @Autowired OpenAiApi openAiApi;

    @Autowired FileHandler fileHandler;

    public ChatHandler(MultipartFile audio, MultipartFile courseMaterial,
                       MultipartFile handWriting, String text){
        this.audio = audio;
        this.courseMaterial = courseMaterial;
        this.handWriting = handWriting;
        this.text = text;
    }

    public String transcript() throws IOException {
        AudioHandler audioHandler = new AudioHandler(openAiAudioApi);
        return audioHandler.audioToEnglish(audio);
    }

    public String summarize() throws IOException {
//        fileHandler.createAssistant();
        String file_id = fileHandler.attach(courseMaterial);
        return fileHandler.sendMessage();
    }
}
