package com.newbini.newbeinquiz.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.newbini.newbeinquiz.web.response.AssistantObject;
import com.newbini.newbeinquiz.web.response.ThreadObject;
import com.newbini.newbeinquiz.web.response.VectorStoreObject;
import org.apache.tomcat.util.json.ParseException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AssistantGeneratorTest {

    AssistantGenerator assistantGenerator = new AssistantGenerator("sk-proj-TCl0PADVZBOfOk8dRjSNT3BlbkFJzl62k0eGjBAfRI5DZ64I");

    @Test
    public void assistantCreateTest() throws JsonProcessingException, ParseException {
        AssistantObject assistant = assistantGenerator.createAssistant("객관식","중간");
        System.out.println("assistant = " + assistant);
    }
    
    @Test
    public void threadCreateTest() throws JsonProcessingException {
        ThreadObject thread = assistantGenerator.createThread();
        System.out.println("thread = " + thread);
    }
    
    @Test
    public void vectorStoreCreateTest() throws JsonProcessingException {
        VectorStoreObject vectorStore = assistantGenerator.createVectorStore();
        System.out.println("vectorStore = " + vectorStore);
    }

}