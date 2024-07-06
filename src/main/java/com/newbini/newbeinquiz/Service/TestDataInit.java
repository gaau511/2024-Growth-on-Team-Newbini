package com.newbini.newbeinquiz.Service;


import com.newbini.newbeinquiz.member.Member;
import com.newbini.newbeinquiz.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

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
}