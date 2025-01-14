package com.newbini.quizard.client.chatgpt;


import com.newbini.quizard.dto.request.QuizCreateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.newbini.quizard.client.chatgpt.ChatGptConst.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatGptClient {

    private final OpenAiChatClient openAiChatClient;

    public ResponseEntity<ChatResponse> sendRequest(QuizCreateRequest quizCreateRequest) {
        ChatOptions options = OpenAiChatOptions.builder()
                .withModel("gpt-4o")
                .withTemperature(0.7f)
                .withResponseFormat(new OpenAiApi.ChatCompletionRequest.ResponseFormat("json_object"))
                .build();

        Message initMessage = new AssistantMessage(INSTRUCTIONS);

        String customizedContent = String.format("학생의 요구사항은 다음과 같습니다.\n" +
                        "\t* 학생의 나이는 %d세입니다.\n" +
                        "\t* 퀴즈의 난이도는 %s입니다.\n" +
                        "\t* 퀴즈의 유형은 %s입니다.\n" +
                        "\t* 퀴즈의 문항은 총 **%d**개 입니다.",
                quizCreateRequest.getAge(), quizCreateRequest.getDifficulty(), quizCreateRequest.getTypes().toString(), quizCreateRequest.getCount());

        Message customizedMessage = new AssistantMessage(customizedContent);

        List<UserMessage> userMessages = quizCreateRequest.getFiles().stream().map(file ->
            new UserMessage(file.getResource())).toList();


        List<Message> messageList = new ArrayList<>();
        messageList.add(initMessage);
        messageList.add(customizedMessage);
        messageList.addAll(userMessages);

        Prompt prompt = new Prompt(messageList, options);

        ChatResponse chatResponse = this.openAiChatClient.call(prompt);
        ResponseEntity<ChatResponse> response = new ResponseEntity<>(chatResponse, HttpStatus.OK);

        log.info("response = {}", response);
        return response;
    }

}
