package com.newbini.newbeinquiz.controller;


import com.newbini.newbeinquiz.Repository.BookmarkRepository;
import com.newbini.newbeinquiz.dto.request.QuizForm;
import com.newbini.newbeinquiz.member.Member;
import com.newbini.newbeinquiz.member.TemporalQuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@Controller
@Service
@RequiredArgsConstructor
public class BookmarkController {

    private final TemporalQuizRepository temporalQuizRepository;
    private final BookmarkRepository bookmarkRepository;

    @GetMapping("/bookmark")
    public String bookmarkPage() {
        return "bookmark";
    }

    @PostMapping("/bookmark")
    @ResponseBody
    public Integer bookmark(@RequestParam("index") Integer index,
                            @SessionAttribute(name = "loginMember", required = false) Member loginMember) {
        //result 화면에서 index를 넘겨받음
        //temporal quiz repository에서 해당하는 quiz를 bookmarkRepository로 넘겨주어야 함

        QuizForm quiz = temporalQuizRepository.findByUuid(loginMember.getUuid()).get();
        QuizForm.Question question = quiz.getQuestions().get(index);
        bookmarkRepository.add(loginMember.getUuid(), question);

        return index;
    }
}
