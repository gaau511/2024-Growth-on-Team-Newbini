package com.newbini.newbeinquiz.dto.response;

import java.util.List;
import java.util.Map;
import lombok.Data;

/**
 * DTO for Assistant Creation
 */

@Data
public class AssistantObject {
    private String id;
    private String object;
    private long created_at;
    private String name;
    private String description;
    private String model;
    private String instructions;
    private List<Tool> tools;
    private Map<String, Object> metadata;
    private double top_p;
    private double temperature;
    private ToolResources tool_resources;

    // response_format의 type은 String or Object 가 될 수 있음
    private Object response_format;


    @Data
    public static class Tool {
        private String type;
    }

    @Data
    public static class ToolResources {
        private FileSearch file_search;
    }

    @Data
    public static class FileSearch {
        private List<String> vector_store_ids;
    }


}