package com.newbini.newbeinquiz.dto.response;

import lombok.Data;

@Data
public class ThreadObject {
    private String id;
    private String object;
    private long created_at;
    private Object metadata;
    private Object tool_resources;
}