package com.newbini.newbeinquiz.api;

import com.newbini.newbeinquiz.web.response.AssistantObject;
import com.newbini.newbeinquiz.web.response.MessageObject;
import com.newbini.newbeinquiz.web.response.ThreadObject;
import org.apache.tomcat.util.json.ParseException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

class ExecuteManagerTest {
    String key = "sk-proj-TCl0PADVZBOfOk8dRjSNT3BlbkFJzl62k0eGjBAfRI5DZ64I";
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
        messageGenerator.attachForTest(file);
        MessageObject message = messageGenerator.createMessage(thread_id);
        executeManager.run(thread_id, assistant_id);
    }
}