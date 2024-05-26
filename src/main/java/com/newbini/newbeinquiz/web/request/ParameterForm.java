package com.newbini.newbeinquiz.web.request;

import lombok.Data;

import java.util.List;

@Data
public class ParameterForm {
    List<String> problem_types;
    String difficulty;
}
