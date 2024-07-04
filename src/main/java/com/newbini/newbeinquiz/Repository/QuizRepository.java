package com.newbini.newbeinquiz.Repository;

import com.newbini.newbeinquiz.domain.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
}
