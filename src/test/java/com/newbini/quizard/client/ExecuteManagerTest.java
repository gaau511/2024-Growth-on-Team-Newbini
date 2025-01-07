package com.newbini.quizard.client;

import com.newbini.quizard.client.chatgpt.assistant.AssistantGenerator;
import com.newbini.quizard.client.chatgpt.thread.ExecuteManager;
import com.newbini.quizard.client.chatgpt.message.MessageGenerator;
import com.newbini.quizard.dto.response.AssistantObject;
import com.newbini.quizard.dto.response.MessageObject;
import com.newbini.quizard.dto.response.ThreadObject;
import org.apache.tomcat.util.json.ParseException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

class ExecuteManagerTest {
    String key = "possible_key";
    MessageGenerator messageGenerator = new MessageGenerator(key,null);
    AssistantGenerator assistantGenerator = new AssistantGenerator(key);
    ExecuteManager executeManager = new ExecuteManager(key);

    ExecuteManagerTest() throws IOException {
    }

    @Test
    void run() throws IOException, InterruptedException, ParseException {
        AssistantObject assistant = assistantGenerator.createAssistant("객관식","보통");
        String assistant_id = assistant.getId();
        ThreadObject thread = assistantGenerator.createThread();
        String thread_id = thread.getId();

        File file = new File("test_sample.txt");
        messageGenerator.attach(file);
        MessageObject message = messageGenerator.createMessage(thread_id);
        executeManager.run(thread_id, assistant_id);
    }
}