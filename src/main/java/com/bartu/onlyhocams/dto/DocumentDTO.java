package com.bartu.onlyhocams.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentDTO {

    private Long id;


    private String fileName;
    private String fileType;


    private byte[] data;

}
