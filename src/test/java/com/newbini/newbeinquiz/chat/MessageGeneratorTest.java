package com.newbini.newbeinquiz.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.newbini.newbeinquiz.web.response.FileObject;
import com.newbini.newbeinquiz.web.response.MessageObject;
import com.newbini.newbeinquiz.web.response.ThreadObject;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class MessageGeneratorTest {

    MessageGenerator messageGenerator = new MessageGenerator("sk-proj-TCl0PADVZBOfOk8dRjSNT3BlbkFJzl62k0eGjBAfRI5DZ64I");
    AssistantGenerator assistantGenerator = new AssistantGenerator("sk-proj-TCl0PADVZBOfOk8dRjSNT3BlbkFJzl62k0eGjBAfRI5DZ64I");
    
    @Test
    void attach() throws IOException {
        File file = new File("C:\\Users\\82109\\Desktop\\Newbini\\src\\main\\resources\\files\\[학습자료] 2주차 인간의 자기이해는 왜 중요한가.pdf");
        FileObject fileObject = messageGenerator.attachForTest(file);
        System.out.println("fileObject = " + fileObject);
    }
    
    @Test 
    void createMessage() throws IOException {
        File file = new File("C:\\Users\\82109\\Desktop\\Newbini\\src\\main\\resources\\files\\[학습자료] 2주차 인간의 자기이해는 왜 중요한가.pdf");
        messageGenerator.attachForTest(file);
        ThreadObject thread = assistantGenerator.createThread();
        String thread_id = thread.getId();
        MessageObject message = messageGenerator.createMessage(thread_id);
        System.out.println("message = " + message);

    }


}