package com.newbini.newbeinquiz.web.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class AssistantCreateResponse {
    private String id;
    private String object;
    private long created_at;
    private String name;
    private String description;
    private String model;
    private String instructions;
    private List<Tool> tools;
    private double top_p;
    private double temperature;
    private ToolResources tool_resources;
    private Map<String, Object> metadata;
    private String response_format;

    public static class Tool {
        private String type;

        // Getters and setters for Tool
        @JsonProperty("type")
        public String getType() {
            return type;
        }

        @JsonProperty("type")
        public void setType(String type) {
            this.type = type;
        }
    }

    public static class ToolResources {
        private FileSearch fileSearch;

        public static class FileSearch {
            private List<String> vectorStoreIds;

            // Getters and setters for CodeInterpreter
            @JsonProperty("vector_store_ids")
            public List<String> getVectorStoreIds() {
                return vectorStoreIds;
            }

            @JsonProperty("vector_store_ids")
            public void setVectorStoreIds(List<String> vectorStoreIds) {
                this.vectorStoreIds = vectorStoreIds;
            }
        }

        // Getters and setters for ToolResources
        @JsonProperty("file_search")
        public FileSearch getFileSearch() {
            return fileSearch;
        }

        @JsonProperty("file_search")
        public void setFileSearch(FileSearch fileSearch) {
            this.fileSearch = fileSearch;
        }

    }

}