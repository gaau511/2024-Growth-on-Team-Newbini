package com.newbini.newbeinquiz.service;


import com.newbini.newbeinquiz.repository.MemberRepository;
import com.newbini.newbeinquiz.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Profile("local")
@Component
public class TestDataInit {
    private final MemberRepository memberRepository;
    /**
     * 테스트 용 초기 회원 추가
     */
    @EventListener(ApplicationReadyEvent.class)
    public void initData() {
        log.info("test data init");
        // 최초 한번만 실행
//        memberRepository.save(new Member("test1","1234", "jenny"));
//        memberRepository.save(new Member("test2", "1234", "john"));
//        memberRepository.save(new Member("test3", "1234", "jane"));
    }

    @Service
    @RequiredArgsConstructor
    public static class MemberService {

        private final MemberRepository memberRepository;

        @Transactional
        public Member join(Member member) {
            memberRepository.save(member);
            return member;
        }



    }
}