package com.newbini.quizard.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login_id", length = 20)
    private String loginId;
    private String password;
    private String name;
    private String latest = null;

    public Member() {
    }

    public Member(String loginId, String password, String name) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
    }
}
