package com.newbini.newbeinquiz.dto.response;

import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class MessageObject {
    private String id;
    private String object;
    private long created_at;
    private String thread_id;
    private String role;
    private List<Content> content;
    private String assistant_id;
    private String run_id;
    private List<Object> attachments;
    private Map<String, Object> metadata;

    @Data
    public static class Content {
        private String type;
        private Text text;
    }

    @Data
    public static class Text {
        private String value;
        private List<Object> annotations;
    }

}
