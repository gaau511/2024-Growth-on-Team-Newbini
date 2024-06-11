package com.newbini.newbeinquiz.controller;

import com.newbini.newbeinquiz.member.TemporalQuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Service
@RequiredArgsConstructor
public class BookmarkController {

    private final TemporalQuizRepository temporalQuizRepository;

    @GetMapping("/bookmark")
    public String bookmarkPage() {
        return "bookmark";
    }

    @PostMapping("/bookmark")
    @ResponseBody
    public Integer bookmark(@RequestParam("index") Integer index) {
        //result 화면에서 index를 넘겨받음
        //temporal quiz repository에서 해당하는 quiz를 bookmarkRepository로 넘겨주어야 함

        return index;

    }
}
