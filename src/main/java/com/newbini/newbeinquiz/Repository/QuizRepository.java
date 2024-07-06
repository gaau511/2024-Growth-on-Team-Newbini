package com.newbini.newbeinquiz.Repository;

import com.newbini.newbeinquiz.domain.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuizRepository extends JpaRepository<Quiz, Long> {

    Optional<Quiz> findByQuizHashAndQuizIndex(String quizHash, Integer index);
}
