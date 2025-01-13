package com.newbini.quizard.client.chatgpt;

import org.junit.jupiter.api.Test;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static org.junit.jupiter.api.Assertions.*;

class ChatGptClientTest {
    private ChatGptClient chatGptClient = new ChatGptClient(new OpenAiChatClient(new OpenAiApi(System.getenv("OPEN_AI_API_KEY"))));

    @Test
    void init() {
        chatGptClient.init();
    }
}