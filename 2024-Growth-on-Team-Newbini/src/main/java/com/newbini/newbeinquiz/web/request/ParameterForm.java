package com.newbini.newbeinquiz.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class ParameterForm {
    List<String> problem_types;
    String difficulty;
}
