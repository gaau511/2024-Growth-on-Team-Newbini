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
    Map<UUID, QuizForm> temporalQuizStore = new HashMap<>();

    public void storeQuiz(UUID uuid, QuizForm quiz) {
        if (temporalQuizStore.containsKey(uuid)) {
            temporalQuizStore.replace(uuid, quiz);
        }
        else {
            temporalQuizStore.put(uuid, quiz);
        }

        log.info("temporal quiz store success");
        log.info("temporalQuizStore = {}", temporalQuizStore);
    }

    public Optional<QuizForm> findByUuid(UUID uuid) {
        return Optional.ofNullable(temporalQuizStore.get(uuid));
    }
}
