package com.newbini.newbeinquiz.dto.response;

import lombok.Data;

@Data
public class DeleteAssistantObject {
    private String id;
    private String object;
    private Boolean deleted;
}
