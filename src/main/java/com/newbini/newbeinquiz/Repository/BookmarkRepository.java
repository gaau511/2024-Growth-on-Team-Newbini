package com.newbini.newbeinquiz.Repository;

import com.newbini.newbeinquiz.dto.request.QuizForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@Repository
public class BookmarkRepository {

    private final static Map<UUID, List<QuizForm.Question>> store = new HashMap<>();

    public void add(UUID uuid, QuizForm.Question question) {
        if (store.containsKey(uuid)) {
            store.get(uuid).add(question);
        }
        else {
            List<QuizForm.Question> questions = new ArrayList<>();
            questions.add(question);
            store.put(uuid, questions);
        }

        log.info("Bookmark success");
        log.info("Bookmark = {}", store);
    }
}
