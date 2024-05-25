package com.bartu.onlyhocams.api.request;

import lombok.Data;

import java.util.List;

@Data
public class PostRequest {
    private String content;
    private Long category;
    private List<Long> documentIds;
}
