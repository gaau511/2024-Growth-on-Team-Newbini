package com.newbini.newbeinquiz.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member, Long>{
    Optional<Member> findByLoginId(String loginId);
}
