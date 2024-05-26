package com.newbini.newbeinquiz.chat;

import com.newbini.newbeinquiz.web.response.AssistantObject;
import com.newbini.newbeinquiz.web.response.MessageObject;
import com.newbini.newbeinquiz.web.response.ThreadObject;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ExecuteManagerTest {
    String apiKey = "sk-proj-TCl0PADVZBOfOk8dRjSNT3BlbkFJzl62k0eGjBAfRI5DZ64I";
    MessageGenerator messageGenerator = new MessageGenerator("sk-proj-TCl0PADVZBOfOk8dRjSNT3BlbkFJzl62k0eGjBAfRI5DZ64I",null);
    AssistantGenerator assistantGenerator = new AssistantGenerator("sk-proj-TCl0PADVZBOfOk8dRjSNT3BlbkFJzl62k0eGjBAfRI5DZ64I");
    ExecuteManager executeManager = new ExecuteManager(apiKey);

    ExecuteManagerTest() throws IOException {
    }

    @Test
    void run() throws IOException, InterruptedException {
        AssistantObject assistant = assistantGenerator.createAssistant();
        String assistant_id = assistant.getId();
        ThreadObject thread = assistantGenerator.createThread();
        String thread_id = thread.getId();

        File file = new File("C:\\Users\\82109\\Desktop\\Newbini\\src\\main\\resources\\files\\[학습자료] 2주차 인간의 자기이해는 왜 중요한가.pdf");
        messageGenerator.attachForTest(file);
        MessageObject message = messageGenerator.createMessage(thread_id);
        executeManager.run(thread_id, assistant_id);
    }
}