package com.newbini.newbeinquiz.dto.response;

import lombok.Data;

/**
 * DTO for Thread Creation
 */

@Data
public class ThreadObject {
    private String id;
    private String object;
    private long created_at;
    private Object metadata;
    private Object tool_resources;
}