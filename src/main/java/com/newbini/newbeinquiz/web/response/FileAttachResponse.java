package com.newbini.newbeinquiz.web.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileAttachResponse {
    String object;
    String id;
    String purpose;
    String filename;
    Long bytes;
    Long created_at;
    String status;
    String status_details;
}
