package com.newbini.newbeinquiz.member;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class MemoryMemberRepository implements MemberRepository {
    private static final Map<Long, Member> store = new HashMap<>();

    public Member save(Member member) {
        return store.put(member.getId(), member);
    }

    public Member delete(Member member) {
        return store.remove(member.getId());
    }

    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(store.keySet().stream().filter(key -> key.equals(id)).findAny().get()));
    }

}
