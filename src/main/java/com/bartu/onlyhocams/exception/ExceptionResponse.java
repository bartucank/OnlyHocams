package com.bartu.onlyhocams.exception;

import lombok.AllArgsConstructor;

import java.sql.Timestamp;


@AllArgsConstructor
public class ExceptionResponse {
    private String code;
    private String msg;
    private Timestamp timestamp;

}
