package com.newbini.newbeinquiz.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.openai.api.OpenAiApi.ChatCompletionMessage;
import org.springframework.ai.openai.api.OpenAiApi.ChatCompletionMessage.ToolCall;
import org.springframework.ai.openai.api.OpenAiApi.ChatCompletion;
import org.springframework.ai.openai.api.OpenAiApi.ChatCompletionMessage.ChatCompletionFunction;
import org.springframework.ai.openai.api.OpenAiApi.ChatCompletionRequest;
import org.springframework.ai.openai.api.OpenAiApi.ChatCompletionMessage.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final OpenAiChatClient chatClient;
    private final OpenAiApi openAiApi;


//    @GetMapping("/ai/generate")
    public Map generate(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        return Map.of("generation", chatClient.call(message));
    }

    @GetMapping("/ai/generate")
    public ResponseEntity<ChatCompletion> chatRequestCreate() {
        ChatCompletionFunction function = new ChatCompletionFunction("file_search","음성 254.mp3");
        ToolCall toolCall = new ToolCall("first-message", "function", function);
        List<ToolCall> toolCalls = new ArrayList<>();
        toolCalls.add(toolCall);


        ChatCompletionMessage defaultMessage = new ChatCompletionMessage("프롬프트가 들어가야 하는 자리입니다.", Role.ASSISTANT,"QuizGenerator", toolCall.id(), toolCalls);
        List<ChatCompletionMessage> messages = new ArrayList<>();
        messages.add(defaultMessage);
        ChatCompletionRequest request = new ChatCompletionRequest(messages,false);

        ResponseEntity<ChatCompletion> response = openAiApi.chatCompletionEntity(
                new ChatCompletionRequest(messages,"gpt-4o-2024-05-13",0.8f, false));

        return response;
    }


}