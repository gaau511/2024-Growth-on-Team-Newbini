package com.newbini.newbeinquiz.dto.request;

import lombok.Data;

import java.util.List;

/**
 * DTO for quiz type
 */

@Data
public class ParameterForm {
    List<String> problem_types;
    String difficulty;
}
