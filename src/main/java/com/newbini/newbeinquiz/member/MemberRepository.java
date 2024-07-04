package com.newbini.newbeinquiz.member;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


public interface MemberRepository {
    Member save(Member member);
//    Optional<Member> findById(Long id);
    Optional<Member> findById(Long id);
    Member delete(Member member);
//    List<Member> findAll();
}
