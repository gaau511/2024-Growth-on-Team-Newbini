package com.newbini.quizard.dto.response;

import lombok.Data;

import java.util.List;

/**
 * DTO for API result receive
 */

@Data
public class ResultObject {
    private String object;
    private List<MessageData> data;
    private String first_id;
    private String last_id;
    private boolean has_more;

    @Data
    public static class MessageData {
        private String id;
        private String object;
        private long created_at;
        private String assistant_id;
        private String thread_id;
        private String run_id;
        private String role;
        private List<Content> content;
        private List<Object> attachments;
        private Object metadata;
    }

    @Data
    public static class Content {
        private String type;
        private TextData text;
    }

    @Data
    public static class TextData {
        private String value;
        private List<Object> annotations;
    }
}
