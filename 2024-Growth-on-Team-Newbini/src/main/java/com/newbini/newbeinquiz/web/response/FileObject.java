package com.newbini.newbeinquiz.dto.response;

import lombok.Data;

@Data
public class FileObject {

    private String id;
    private String object;
    private long bytes;
    private long created_at;
    private String filename;
    private String purpose;
    private String status;
    private Object status_details;
}
