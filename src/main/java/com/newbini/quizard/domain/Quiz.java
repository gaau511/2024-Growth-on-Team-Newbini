package com.newbini.quizard.domain;

import lombok.Data;
import jakarta.persistence.*;

@Entity
@Data
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="member_id")
    private Long memberId;

    @Column(name="quiz_hash")
    private String quizHash;

    @Column(name="quiz_index")
    private Integer quizIndex;

    private String question;

    private String answer;

    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Member member;

    public Quiz() {};

    public Quiz(Long memberId, String quizHash, Integer quizIndex, String question, String answer) {
        this.memberId = memberId;
        this.quizHash = quizHash;
        this.quizIndex = quizIndex;
        this.question = question;
        this.answer = answer;
    }
}
