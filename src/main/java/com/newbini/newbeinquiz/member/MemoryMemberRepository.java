package com.newbini.newbeinquiz.member;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class MemoryMemberRepository implements MemberRepository {
    private static final Map<UUID, Member> store = new HashMap<>();

    public Member save(Member member) {
        return store.put(member.getUuid(), member);
    }

    public Optional<Member> delete(Member member) {
        return Optional.ofNullable(store.remove(member.getUuid()));
    }

    public Optional<Member> findByLoginId(String loginId) {
        return store.values().stream().filter(member -> member.getLoginId().equals(loginId)).findAny();
    }

}
