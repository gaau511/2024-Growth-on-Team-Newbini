package com.newbini.quizard.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Bookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="member_id")
    private Long memberId;

    @Column(name="quiz_id")
    private Long quizId;

    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "quiz_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Quiz quiz;

    public Bookmark() {};

    public Bookmark(Long memberId, Long quizHash) {
        this.memberId = memberId;
        this.quizId = quizHash;
    }
}
