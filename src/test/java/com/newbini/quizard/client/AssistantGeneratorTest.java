package com.newbini.quizard.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.tomcat.util.json.ParseException;
import org.junit.jupiter.api.Test;

class AssistantGeneratorTest {

    private final String key = "";
    AssistantGenerator assistantGenerator = new AssistantGenerator(key);

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