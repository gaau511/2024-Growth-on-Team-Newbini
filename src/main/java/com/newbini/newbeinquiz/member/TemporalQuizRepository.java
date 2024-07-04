package com.newbini.newbeinquiz.member;

import com.newbini.newbeinquiz.dto.request.QuizForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * 가장 최근에 생성한 퀴즈 한 set를 저장하는 임시 레포지토리
 */
@Repository
@Slf4j
public class TemporalQuizRepository {
    Map<Long, QuizForm> temporalQuizStore = new HashMap<>();

    public void storeQuiz(Long id, QuizForm quiz) {
        if (temporalQuizStore.containsKey(id)) {
            temporalQuizStore.replace(id, quiz);
        }
        else {
            temporalQuizStore.put(id, quiz);
        }

        log.info("temporal quiz store success");
        log.info("temporalQuizStore = {}", temporalQuizStore);
    }

    public Optional<QuizForm> findById(Long id) {
        return Optional.ofNullable(temporalQuizStore.get(id));
    }
}
