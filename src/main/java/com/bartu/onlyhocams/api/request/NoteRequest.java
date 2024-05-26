package com.bartu.onlyhocams.api.request;

import lombok.Data;

import java.util.List;

@Data
public class NoteRequest {
    private String content;
    private String title;
    private Long documentId;
}
