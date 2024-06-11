package com.newbini.newbeinquiz;

import com.newbini.newbeinquiz.controller.ResultController;
import com.newbini.newbeinquiz.dto.request.QuizForm;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ResultPageTest {

    @BeforeAll
    public static void setUpHeadful() {
        System.setProperty("java.awt.headless", "false");
    }

    @Autowired
    private ResultController resultController;

    @Autowired
    private MockMvc mockMvc;



    @Test
    public void resultPageTest() throws Exception {

        QuizForm quiz = makeMockQuiz();
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/result")
                .flashAttr("quiz", quiz)).andReturn();

        String renderedPage = mvcResult.getResponse().getContentAsString();

        // 렌더링된 HTML 페이지를 임시 파일로 저장
        Path tempFile = Files.createTempFile("thymeleaf-test", ".html");
        Files.write(tempFile, renderedPage.getBytes(StandardCharsets.UTF_8));

        // 웹 브라우저에서 임시 파일 열기
        Desktop.getDesktop().browse(tempFile.toUri());
    }


    private QuizForm makeMockQuiz() {
        QuizForm quiz = new QuizForm();
        List<String> tempOptionList = new ArrayList<>();
        tempOptionList.add("정답");
        tempOptionList.add("오답");
        QuizForm.Question question1 = new QuizForm.Question("객관식","첫번째 객관식 질문", "정답", tempOptionList);
        QuizForm.Question question2 = new QuizForm.Question("O/X","두번째 OX 질문", "O", null);
        QuizForm.Question question3 = new QuizForm.Question("주관식","세번째 주관식 질문", "그럴수도 아닐수도 있다.", null);
        List<QuizForm.Question> questions = new ArrayList<>();
        questions.add(question1);
        questions.add(question2);
        questions.add(question3);
        quiz.setQuestions(questions);

        return quiz;
    }
}
