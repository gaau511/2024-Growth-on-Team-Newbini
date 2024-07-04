package com.newbini.newbeinquiz.member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
@Slf4j
@RequiredArgsConstructor
public class loginController {

    private final MemberRepository memberRepository;

    @GetMapping("/sign-in")
    public String signInForm() {
        return "sign-in";
    }

    @PostMapping("/sign-in")
    public String signIn(@ModelAttribute("member") Member member,
                         HttpServletRequest request) throws Exception {

        Optional<Member> findMember = memberRepository.findById(member.getId());
        if (findMember.isPresent()) {
            log.info("findMemberPassword = {}", findMember.get().getPassword());
            log.info("memberPassword = {}", member.getPassword());

            if (findMember.get().getPassword().equals(member.getPassword())) {
                // 로그인 성공
                HttpSession session = request.getSession();
                session.setAttribute("loginMember", findMember.get());
            }
            else {
                throw new Exception("Wrong password");
            }
        }
        else {
            throw new Exception("Wrong ID");
        }

        return "redirect:/";

    }

}
