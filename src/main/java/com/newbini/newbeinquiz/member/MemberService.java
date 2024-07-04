package com.newbini.newbeinquiz.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemoryMemberRepository memoryMemberRepository;

    public Member join(Member member) throws Exception {
        if (existsByLoginId(member.getLoginId())) {
            throw new Exception("You tried to sign up with a duplicate ID.");
        }

        memoryMemberRepository.save(member);
        return memoryMemberRepository.findByLoginId(member.getLoginId()).get();
    }

    public Boolean existsByLoginId(String loginId) {
        return memoryMemberRepository.findByLoginId(loginId).isPresent();
    }


}
