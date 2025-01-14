package com.newbini.quizard.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuizCreateRequest {
    private Integer age;
    private String difficulty;
    private List<String> types;
    private Integer count;

    private List<MultipartFile> files;
}
