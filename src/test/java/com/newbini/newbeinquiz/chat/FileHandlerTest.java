package com.newbini.newbeinquiz.chat;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FileHandlerTest {

    String apiKey = "sk-proj-TCl0PADVZBOfOk8dRjSNT3BlbkFJzl62k0eGjBAfRI5DZ64I";
    FileHandler fileHandler = new FileHandler(apiKey);
    File file = new File("C:\\Users\\82109\\Desktop\\Newbini\\src\\main\\resources\\files\\[학습자료] 2주차 인간의 자기이해는 왜 중요한가.pdf");

    @Test
    void test() throws IOException {
        fileHandler.create();
        fileHandler.attach(file);
        fileHandler.sendMessage();
        fileHandler.run();
    }

}