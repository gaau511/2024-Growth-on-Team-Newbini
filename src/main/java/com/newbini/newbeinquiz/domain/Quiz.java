package com.newbini.newbeinquiz.domain;

import com.newbini.newbeinquiz.member.Member;
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

    @Column(name="quiz_id")
    private String quizId;

    @Column(name="quiz_index")
    private Integer quizIndex;

    private String question;

    private String answer;

    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Member member;

    public Quiz(Long memberId, String quizId, Integer quizIndex, String question, String answer) {
        this.memberId = memberId;
        this.quizId = quizId;
        this.quizIndex = quizIndex;
        this.question = question;
        this.answer = answer;
    }
}
