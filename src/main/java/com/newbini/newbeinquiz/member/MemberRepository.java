package com.newbini.newbeinquiz.member;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class MemberRepository {
    private static final Map<UUID, Member> store = new HashMap<>();

    public void create(Member member) throws Exception {
        if (idDuplicateCheck(member)) {
            throw new Exception("ID Duplication");
        }

        store.put(member.getUuid(), member);
    }

    public void delete(Member member) {
        store.remove(member.getUuid());
    }

    public Optional<Member> findByLoginId(String loginId) {
        return store.values().stream().filter(member -> member.getLoginId().equals(loginId)).findAny();
    }

    /**
     * @param member
     * @return True 반환 시 중복 ID
     */
    public boolean idDuplicateCheck(Member member) {
        return store.values().stream().anyMatch(m -> m.getLoginId().equals(member.getLoginId()));
    }

}
