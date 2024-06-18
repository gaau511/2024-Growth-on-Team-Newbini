package com.newbini.newbeinquiz.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class RunObject {
    private String id;
    private String object;
    private long created_at;
    private String assistant_id;
    private String thread_id;
    private String status;
    private Long started_at;
    private long expires_at;
    private Long cancelled_at;
    private Long failed_at;
    private Long completed_at;
    private String required_action;
    private ErrorObject last_error;
    private String model;
    private String instructions;
    private List<Tool> tools;
    private Map<String, Object> tool_resources;
    private Map<String, Object> metadata;
    private double temperature;
    private double top_p;
    private Long max_completion_tokens;
    private Long max_prompt_tokens;
    private TruncationStrategy truncation_strategy;
    private String incomplete_details;
    private Map<String, Object> usage;
    private String response_format;
    private String tool_choice;

    @Data
    public static class Tool {
        private String type;
    }

    @Data
    public static class TruncationStrategy {
        private String type;
        private List<String> last_messages;
    }
    @Data
    private static class Format {
        private String type;
    }

    @Data
    private static class ErrorObject {
        private String code;
        private String message;
    }
}