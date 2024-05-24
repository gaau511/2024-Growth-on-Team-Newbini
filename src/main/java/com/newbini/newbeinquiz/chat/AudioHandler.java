package com.newbini.newbeinquiz.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.openai.OpenAiAudioTranscriptionClient;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.ai.openai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.openai.audio.transcription.AudioTranscriptionResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Slf4j
@RequiredArgsConstructor
public class AudioHandler {

    private final OpenAiAudioApi openAiAudioApi;

    @Value("${file.dir}")
    private String uploadDir;

    String audioToEnglish(MultipartFile audioFile) throws IOException {
        Path filePath = saveFile(audioFile);

        OpenAiAudioTranscriptionClient openAiTranscriptionClient = new OpenAiAudioTranscriptionClient(openAiAudioApi);
        OpenAiAudioApi.TranscriptResponseFormat responseFormat = OpenAiAudioApi.TranscriptResponseFormat.SRT;

        OpenAiAudioTranscriptionOptions transcriptionOptions = OpenAiAudioTranscriptionOptions.builder()
                .withLanguage("en")
                .withPrompt("")
                .withTemperature(0.3f)
                .withResponseFormat(responseFormat)
                .build();

        AudioTranscriptionPrompt transcriptionRequest = new AudioTranscriptionPrompt(audioFile.getResource(), transcriptionOptions);
        AudioTranscriptionResponse response = openAiTranscriptionClient.call(transcriptionRequest);

        return response.getResult().getOutput();
    }

    private Path saveFile(MultipartFile file) throws IOException {
        // Ensure the upload directory exists
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(file.getOriginalFilename());
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        log.info("filePath = {}", filePath);
        return filePath;
    }




}
