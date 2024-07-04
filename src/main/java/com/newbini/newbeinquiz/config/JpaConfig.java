package com.newbini.newbeinquiz.config;

import com.newbini.newbeinquiz.member.JpaMemberRepository;
import com.newbini.newbeinquiz.member.MemberRepository;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JpaConfig {

    private final EntityManager em;
    public JpaConfig(EntityManager em) {
        this.em = em;
    }

    @Bean
    public MemberRepository memberRepository() {
        return new JpaMemberRepository(em);
    }

}
