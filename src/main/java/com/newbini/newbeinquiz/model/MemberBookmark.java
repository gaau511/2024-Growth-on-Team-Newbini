package com.newbini.newbeinquiz.model;

import javax.persistence.*;

@Entity
@Table(name = "member_bookmark")
public class MemberBookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmarkID")
    private Long bookmarkID;

    @ManyToOne
    @JoinColumn(name = "memberID", nullable = false)
    private Member member;  // 'member'를 'memberID'로 바꿔도 될까요?

    @Column(name = "question", nullable = false)
    private String question;

    @Column(name = "answer", nullable = false)
    private String answer;

    // Getters and setters
}
