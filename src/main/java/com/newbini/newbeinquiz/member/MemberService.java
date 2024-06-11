package com.newbini.newbeinquiz.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member join(Member member) throws Exception {
        memberRepository.create(member);
        return memberRepository.findByLoginId(member.getLoginId()).get();
    }


}
