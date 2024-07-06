//package com.newbini.newbeinquiz.member;
//
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.TypedQuery;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Optional;
//
//@Slf4j
//@Transactional
//public class JpaMemberRepository implements MemberRepository {
//    private final EntityManager em;
//
//    public JpaMemberRepository(EntityManager em) {
//        this.em = em;
//    }
//
//    @Override
//    public Member save(Member member) {
//        em.persist(member);
//        return member;
//    }
//
//    @Override
//    public Optional<Member> findById(Long id) {
//        Member member = em.find(Member.class, id);
//        return Optional.ofNullable(member);
//    }
//
//    @Override
//    public Member delete(Member member) {
//        em.remove(member);
//        return member;
//    }
//
//    @Override
//    public Optional<Member> findByLoginId(String loginId) {
//        String jpql = "SELECT m FROM Member m WHERE m.loginId = :loginId";
//        TypedQuery<Member> query = em.createQuery(jpql, Member.class);
//        query.setParameter("loginId", loginId);
//        return Optional.ofNullable(query.getSingleResult());
//    }
//
//    @Override
//    public void updateLatest(Long memberId, String latest) {
//        Member findMember = em.find(Member.class, memberId);
//        findMember.setLatest(latest);
//    }
//
//    @Override
//    public void deleteAll() {
//        String jpql = "delete from Member";
//        em.createQuery(jpql).executeUpdate();
//    }
//}
