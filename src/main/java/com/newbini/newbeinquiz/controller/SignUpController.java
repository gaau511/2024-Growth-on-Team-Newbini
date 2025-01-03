package com.newbini.newbeinquiz.controller;

import com.newbini.newbeinquiz.service.TestDataInit;
import com.newbini.newbeinquiz.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SignUpController {

    private final TestDataInit.MemberService memberService;

    @GetMapping("/sign-up")
    public String signUpForm(@ModelAttribute("member") Member member) {
        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String signUp(@ModelAttribute("member") Member member) throws Exception {
        log.info("member = {}", member);
        memberService.join(member);
        return "redirect:/";
    }
}
