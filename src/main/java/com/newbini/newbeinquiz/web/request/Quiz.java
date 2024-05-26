package com.newbini.newbeinquiz.web.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Quiz {

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
