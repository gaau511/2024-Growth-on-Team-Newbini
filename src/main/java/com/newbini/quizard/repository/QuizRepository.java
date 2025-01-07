package com.newbini.quizard.repository;

import com.newbini.quizard.domain.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuizRepository extends JpaRepository<Quiz, Long> {

    Optional<Quiz> findByQuizHashAndQuizIndex(String quizHash, Integer index);
}
