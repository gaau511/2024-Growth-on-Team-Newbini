package com.newbini.newbeinquiz.model;  // 패키지 선언: java파일이 속하는 패키지 지정

import javax.persistence.*;
import java.util.List;

@Entity // class가 JPA entity class임을 선언
@Table(name = "member") // 해당 entity class와 mapping될 db table 이름 지정
public class Member {
    @Id // 해당 field가 table의 primary key임을 선언
    @GeneratedValue(strategy = GenerationType.IDENTITY) // primary key 값 자동 생성 방법 지정 - IDENTIFY 전략 : db가 자동으로 ID값 생성
    @Column(name = "memberID")  // 해당 field가 db의 attribute로 mapping될 때 해당 attribute의 이름, 제약조건 설정하는 어노테이션
    private Long memberID;

    @Column(name = "userID", unique = true, nullable = false)
    private String userID;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "email", nullable = false)
    private String email;

    @OneToMany(mappedBy = "member")
    private List<MemberBookmark> bookmarks;

    // Getters and setters
}
