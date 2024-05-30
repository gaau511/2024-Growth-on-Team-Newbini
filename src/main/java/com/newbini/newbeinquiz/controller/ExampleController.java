package com.newbini.newbeinquiz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 추후 구현 예정인 미구현 컨트롤러
 * test 영상에서만 활용
 */

@Controller
public class ExampleController {

    @GetMapping("/login")
    String loginForm () {
        return "login";
    }

    @GetMapping("/signup")
    String signupForm() {
        return "signup";
    }

    @GetMapping("/quiz-type-choice")
    String quizTypePage() {
        return "quiz-type-choice";
    }

    @GetMapping("/summary")
    String summaryPage() {
        return "summary";
    }

    @GetMapping("/subject-choice")
    String subjectChoicePage() {
        return "subject-choice";
    }
}
