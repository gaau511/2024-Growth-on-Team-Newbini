package com.newbini.newbeinquiz.member;

import lombok.Data;
import java.util.UUID;

@Data
public class Member {
    public Member() {
        this.id = UUID.randomUUID().toString();
    }

    public Member(String loginId, String password, String name) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.id = UUID.randomUUID().toString();
    }

    private String id;
    private String loginId;
    private String password;
    private String name;
}
