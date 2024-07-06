package com.newbini.newbeinquiz.controller;


import com.newbini.newbeinquiz.Repository.BookmarkRepository;
import com.newbini.newbeinquiz.Repository.QuizRepository;
import com.newbini.newbeinquiz.domain.Bookmark;
import com.newbini.newbeinquiz.domain.Quiz;
import com.newbini.newbeinquiz.dto.request.QuizForm;
import com.newbini.newbeinquiz.member.Member;
import com.newbini.newbeinquiz.member.MemberRepository;
import com.newbini.newbeinquiz.member.TemporalQuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class BookmarkController {

    private final MemberRepository memberRepository;
    private final QuizRepository quizRepository;
    private final BookmarkRepository bookmarkRepository;

    @GetMapping("/bookmark")
    public String bookmarkPage(@SessionAttribute(name = "loginMember") Member loginMember,
                               Model model) {

        if (loginMember != null) {
            List<Bookmark> findBookmarkList = bookmarkRepository.findAllByMemberId(loginMember.getId());
            List<Quiz> quizList = new ArrayList<>();
            for (Bookmark findBookmark : findBookmarkList) {
                quizList.add(quizRepository.findById(findBookmark.getQuizId()).get());
            }
            model.addAttribute("quizList", quizList);
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
            Optional<Member> findMember = memberRepository.findById(loginMember.getId());
            String quizHash = findMember.get().getLatest();
            Optional<Quiz> findQuiz = quizRepository.findByQuizHashAndQuizIndex(quizHash, index);
            Bookmark bookmark = new Bookmark(loginMember.getId(), findQuiz.get().getId());
            bookmarkRepository.save(bookmark);
        }
    }
}
