package com.newbini.quizard.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class ListAssistantsObject {
    private String object;
    private List<AssistantObject> data;
    private String first_id;
    private String last_id;
    private Boolean has_more;

}
