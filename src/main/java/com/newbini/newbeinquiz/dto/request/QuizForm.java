package com.newbini.newbeinquiz.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Dto that maps a JsonString responded from the API to an object
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizForm {

    private List<Question> questions = new ArrayList<>();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Question {
        private String type;
        private String question;
        private Object answer;
        private List<String> options = new ArrayList<>();
    }
}
