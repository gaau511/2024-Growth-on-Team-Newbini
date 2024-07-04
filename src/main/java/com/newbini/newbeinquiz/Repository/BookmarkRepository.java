package com.newbini.newbeinquiz.Repository;

import com.newbini.newbeinquiz.dto.request.QuizForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@Repository
public class BookmarkRepository {

    private final static Map<Long, List<QuizForm.Question>> store = new HashMap<>();

    public void add(Long id, QuizForm.Question question) {
        if (store.containsKey(id)) {
            store.get(id).add(question);
        }
        else {
            List<QuizForm.Question> questions = new ArrayList<>();
            questions.add(question);
            store.put(id, questions);
        }

        log.info("Bookmark success");
        log.info("Bookmark = {}", store);
    }

    public List<QuizForm.Question> findQuiz(Long id) {
        return store.get(id);
    }
}
