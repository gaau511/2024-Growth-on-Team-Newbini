package com.newbini.newbeinquiz.member;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class MemoryMemberRepository implements MemberRepository {
    private static final Map<Long, Member> store = new HashMap<>();

    @Override
    public Member save(Member member) {
        return store.put(member.getId(), member);
    }

    @Override
    public Member delete(Member member) {
        return store.remove(member.getId());
    }

    @Override
    public Optional<Member> findByLoginId(String loginId) {
        return store.values().stream().filter(m -> m.getLoginId() == loginId).findAny();
    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public void deleteAll() {
        store.clear();
    }

}
