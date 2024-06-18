package com.newbini.newbeinquiz.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SignUpController {

    private final MemberService memberService;

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
