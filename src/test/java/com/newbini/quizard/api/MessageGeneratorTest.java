package com.newbini.quizard.api;

import com.newbini.quizard.dto.response.MessageObject;
import com.newbini.quizard.dto.response.ThreadObject;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


class MessageGeneratorTest {
    String key = "";
    List<File> fileList = new ArrayList<>();
    AssistantGenerator assistantGenerator = new AssistantGenerator(key);

    @Test 
    void createMessage() throws IOException {
        File file = new File("test_sample.txt");
        fileList.add(file);
        MessageGenerator mg = new MessageGenerator(key, fileList);

        ThreadObject thread = assistantGenerator.createThread();
        String thread_id = thread.getId();
        MessageObject message = mg.createMessage(thread_id);
        System.out.println("message = " + message);

    }


}