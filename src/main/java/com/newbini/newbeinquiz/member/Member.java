package com.newbini.newbeinquiz.member;

import lombok.Data;
import java.util.UUID;

@Data
public class Member {
    public Member() {
        this.uuid = UUID.randomUUID();
    }

    public Member(String loginId, String password, String name) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;

        this.uuid = UUID.randomUUID();
    }

    private UUID uuid;
    private String loginId;
    private String password;
    private String name;
}
