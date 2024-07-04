package com.newbini.newbeinquiz.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


public interface MemberRepository {
    Member save(Member member);
    Optional<Member> findById(Long id);
    Member delete(Member member);
    Optional<Member> findByLoginId(String loginId);
    void deleteAll();
//    List<Member> findAll();
}
