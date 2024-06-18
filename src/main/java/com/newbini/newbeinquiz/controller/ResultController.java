package com.newbini.newbeinquiz.controller;

import com.newbini.newbeinquiz.dto.request.QuizForm;
import com.newbini.newbeinquiz.member.Member;
import com.newbini.newbeinquiz.member.TemporalQuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/result")
@RequiredArgsConstructor
public class ResultController {

    private final TemporalQuizRepository temporalQuizRepository;


    @GetMapping
    public String showResult() {
        return "result";
    }

//    @GetMapping
    public String tempShowResult(Model model,
                                 @SessionAttribute(name = "loginMember", required = false) Member loginMember) {
        QuizForm quiz = makeMockQuiz();
        if (loginMember != null) {
            temporalQuizRepository.storeQuiz(loginMember.getUuid(), quiz);
        }
        model.addAttribute("quiz", quiz);
        return "result";
    }

    private QuizForm makeMockQuiz() {
        QuizForm quiz = new QuizForm();
        List<String> tempOptionList = new ArrayList<>();
        tempOptionList.add("정답");
        tempOptionList.add("오답");
        QuizForm.Question question1 = new QuizForm.Question("객관식","첫번째 객관식 질문", "정답", tempOptionList);
        QuizForm.Question question2 = new QuizForm.Question("O/X","두번째 OX 질문", "O", null);
        QuizForm.Question question3 = new QuizForm.Question("주관식","세번째 주관식 질문", "Claude", null);
        List<QuizForm.Question> questions = new ArrayList<>();
        questions.add(question1);
        questions.add(question2);
        questions.add(question3);
        quiz.setQuestions(questions);

        return quiz;
    }
}
