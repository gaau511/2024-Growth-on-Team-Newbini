package com.newbini.newbeinquiz.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemoryMemberRepository memoryMemberRepository;

    public Member join(Member member) throws Exception {
        memoryMemberRepository.save(member);
        return memoryMemberRepository.findById(member.getId()).get();
    }



}
