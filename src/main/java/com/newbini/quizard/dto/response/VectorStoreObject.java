package com.newbini.quizard.dto.response;

import lombok.Data;

/**
 * DTO for Vector Store Creation
 */

@Data
public class VectorStoreObject {
    private String id;
    private String object;
    private String name;
    private String status;
    private int usage_bytes;
    private long created_at;
    private FileCounts file_counts;
    private Object metadata;
    private Object expires_after;
    private Object expires_at;
    private long last_active_at;

    @Data
    public static class FileCounts {
        private int in_progress;
        private int completed;
        private int failed;
        private int cancelled;
        private int total;
    }
}