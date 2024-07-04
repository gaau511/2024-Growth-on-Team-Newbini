package com.newbini.newbeinquiz.member;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository {
    Member save(Member member);
//    Optional<Member> findById(Long id);
    Optional<Member> findByLoginId(String loginId);
    Optional<Member> delete(Member member);
//    List<Member> findAll();
}
