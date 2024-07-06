package com.newbini.newbeinquiz.controller;


import com.newbini.newbeinquiz.Repository.BookmarkRepository;
import com.newbini.newbeinquiz.dto.request.QuizForm;
import com.newbini.newbeinquiz.member.Member;
import com.newbini.newbeinquiz.member.TemporalQuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookmarkController {

    private final TemporalQuizRepository temporalQuizRepository;
    private final BookmarkRepository bookmarkRepository;

    @GetMapping("/bookmark")
    public String bookmarkPage(@SessionAttribute(name = "loginMember") Member loginMember,
                               Model model) {

        if (loginMember != null) {
            List<QuizForm.Question> quizQuestions = bookmarkRepository.findQuiz(loginMember.getId());
            QuizForm quiz = new QuizForm();
            quiz.setQuestions(quizQuestions);
            model.addAttribute("quiz", quiz);
        }

        return "bookmark";
    }

    @PostMapping("/bookmark")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public void bookmark(@RequestParam("index") Integer index,
                            @SessionAttribute("loginMember") Member loginMember) {
        //result 화면에서 index를 넘겨받음
        //temporal quiz repository에서 해당하는 quiz를 bookmarkRepository로 넘겨주어야 함
        if (loginMember != null) {
            QuizForm quiz = temporalQuizRepository.findById(loginMember.getId()).get();
            QuizForm.Question question = quiz.getQuestions().get(index);
            bookmarkRepository.add(loginMember.getId(), question);
        }
    }
}
