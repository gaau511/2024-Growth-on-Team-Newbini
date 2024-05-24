package com.newbini.newbeinquiz.web.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class FileSummaryResponse {

    private String id;
    private String object;
    private long created_at;
    private String assistant_id;
    private String thread_id;
    private String run_id;
    private String role;
    private List<Content> content;
    private List<Map<String, Object>> attachments;
    private Map<String, Object> metadata;


    public static class Content {
        private String type;
        private TextData text;

        public static class TextData {
            private String value;
            private List<Map<String, Object>> annotations;

            // Getters and setters for TextData
            @JsonProperty("value")
            public String getValue() {
                return value;
            }

            @JsonProperty("value")
            public void setValue(String value) {
                this.value = value;
            }

            @JsonProperty("annotations")
            public List<Map<String, Object>> getAnnotations() {
                return annotations;
            }

            @JsonProperty("annotations")
            public void setAnnotations(List<Map<String, Object>> annotations) {
                this.annotations = annotations;
            }
        }

        // Getters and setters for Content
        @JsonProperty("type")
        public String getType() {
            return type;
        }

        @JsonProperty("type")
        public void setType(String type) {
            this.type = type;
        }

        @JsonProperty("text")
        public TextData getText() {
            return text;
        }

        @JsonProperty("text")
        public void setText(TextData text) {
            this.text = text;
        }
    }


}
