package com.newbini.newbeinquiz.web.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ThreadResponse {

    String id;
    String object;
    Long created_at;
    Object metadata;
    Object tool_resources;

}
