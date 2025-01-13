package com.newbini.quizard.client.chatgpt;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.ChatOptionsBuilder;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.ModelRequest;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import static com.newbini.quizard.client.chatgpt.ChatGptConst.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatGptClient {

    private final OpenAiChatClient openAiChatClient;

    public ResponseEntity<ChatResponse> init() {
        ChatOptions options = OpenAiChatOptions.builder()
                .withModel("gpt-4o")
                .withTemperature(0.7f)
                .withResponseFormat(new OpenAiApi.ChatCompletionRequest.ResponseFormat("json_object"))
                .build();

        Message message = new AssistantMessage(INSTRUCTIONS);
        Prompt prompt = new Prompt(message, options);

        ChatResponse initResponse = this.openAiChatClient.call(prompt);
        ResponseEntity<ChatResponse> response = new ResponseEntity<>(initResponse, HttpStatus.OK);

        log.info("response = {}", response);
        return response;
    }
}
